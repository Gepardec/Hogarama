import {Injectable} from '@angular/core';
import {HogaramaBackendService} from '../HogaramaBackendService/hogarama-backend.service';

export class MessageAction {
    constructor(
        public text: string | undefined,
        public confirmReply: string | undefined,
        public abortReply: string | undefined,
        public operation: string | undefined,
        public dto: any | undefined
    ) {
    }

    static createAssistantMessage(content: string | undefined) {
        return new Message('assistant', content);
    }

    static createUserMessage(content: string | undefined) {
        return new Message('user', content);
    }

}

export class Message {
    constructor(
        public role: 'user' | 'assistant',
        public content: string | undefined,
        public decision?: 'confirmed' | 'aborted' | undefined,
        public action?: MessageAction | undefined
    ) {
    }

    static createAssistantMessage(content: string | undefined) {
        return new Message('assistant', content);
    }

    static createUserMessage(content: string | undefined) {
        return new Message('user', content);
    }

    applyConfirm() {
        if (this.action !== undefined) {
            this.content = this.action.confirmReply;
            const action = this.action;
            this.action = undefined;
            this.decision = 'confirmed';
            return action;
        }
    }

    error(error: string) {
        if (this.action !== undefined) {
            this.content = error;
            this.action = undefined;
            this.decision = 'aborted';
        }
    }

    applyAbort() {
        if (this.action !== undefined) {
            this.content = this.action.abortReply;
            this.action = undefined;
            this.decision = 'aborted';
        }
    }

}

export class Dialog {

    messages: Message[];

    constructor(prompts: Message[]) {
        this.messages = prompts;
    }

    addMessage(message: Message): void {
        this.messages.push(message);
    }

    addMessages(messages: Message[]) {
        this.messages.concat(messages);
    }

    getLastMessage() {
        return this.messages[this.messages.length - 1];
    }

    clearMessages() {
        // clear all messages without creating a new array
        this.messages.splice(0, this.messages.length);
    }
}

@Injectable({
    providedIn: 'root'
})
export class AIRestService {
    constructor(private backend: HogaramaBackendService) {
    }

    async chat(dialog: Dialog) {
        try {
            const message = await this.backend.chat.chat(dialog.messages.map(m => ({
                role: m.role.toUpperCase(),
                content: m.content || '',  // Assuming content should default to an empty string if undefined
                action: undefined
            })));
            if(message.role.toLowerCase() === 'assistant') {
                const answerMessage = Message.createAssistantMessage(message.content);
                if(message.action !== undefined) {
                    answerMessage.action = message.action;
                }
                dialog.addMessage(answerMessage);
            } else {
                dialog.addMessage(Message.createAssistantMessage('Ups, something went wrong. Returned message has type ' + message.role + ' and content ' + message.content + '.'));
            }
        } catch (e) {
            dialog.addMessage(Message.createAssistantMessage('Ups, something went wrong. Exception occured ' + e));
        }

        // if (dialog.getLastMessage().content === 'act') {
        //     // simulate answer with action
        //     dialog.addMessage(Message.createAssistantMessage(JSON.stringify({
        //         type: 'confirmation',
        //         description: 'delete the rule',
        //         params: {
        //             confirmReply: 'The action \'delete the rule\' has been performed.',
        //             abortReply: 'You aborted the action \'delete the rule\'',
        //         }
        //     })));
        //
        //     // if the last message is json string, then it is an action
        //     if (dialog.getLastMessage().content && dialog.getLastMessage().content.startsWith('{')) {
        //         const action = JSON.parse(dialog.getLastMessage().content);
        //         dialog.getLastMessage().action = new MessageAction('You are about to perform the action: ' + action.description,
        //             action.params.confirmReply,
        //             action.params.abortReply);
        //     }
        // } else {
        //     dialog.addMessage(Message.createAssistantMessage('Hello! You wrote: ' + dialog.getLastMessage().content + '.'));
        // }
    }
}

@Injectable({
    providedIn: 'root'
})
export class ChatService {

    dialog: Dialog = new Dialog([]);

    async say(message: string) {
        this.dialog.messages.push(Message.createUserMessage(message));
        await this.aiChatService.chat(this.dialog);
    }

    constructor(private aiChatService: AIRestService) {
    }
}

function delay(ms: number): Promise<void> {
    return new Promise<void>(resolve => setTimeout(resolve, ms));
}