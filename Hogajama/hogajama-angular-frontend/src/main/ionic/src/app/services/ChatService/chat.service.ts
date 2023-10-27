import {Injectable} from '@angular/core';

export class MessageAction {
    constructor(
        public text: string | undefined,
        public confirmReply: string | undefined,
        public abortReply: string | undefined,
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
            this.action = undefined;
            this.decision = 'confirmed';
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
}

export class AIRestService {
    constructor() {
    }

    async chat(dialog: Dialog) {
        await delay(1000);
        if (dialog.getLastMessage().content === 'act') {
            // simulate answer with action
            dialog.addMessage(Message.createAssistantMessage(JSON.stringify({
                type: 'confirmation',
                description: 'delete the rule',
                params: {
                    confirmReply: 'The action \'delete the rule\' has been performed.',
                    abortReply: 'You aborted the action \'delete the rule\'',
                }
            })));

            // if the last message is json string, then it is an action
            if (dialog.getLastMessage().content && dialog.getLastMessage().content.startsWith('{')) {
                const action = JSON.parse(dialog.getLastMessage().content);
                dialog.getLastMessage().action = new MessageAction('You are about to perform the action: ' + action.description,
                    action.params.confirmReply,
                    action.params.abortReply);
            }
        } else {
            dialog.addMessage(Message.createAssistantMessage('Hello! You wrote: ' + dialog.getLastMessage().content + '.'));
        }
    }
}

@Injectable({
    providedIn: 'root'
})
export class ChatService {

    dialog: Dialog = new Dialog([]);
    aiChatService: AIRestService = new AIRestService();

    async say(message: string) {
        this.dialog.messages.push(Message.createUserMessage(message));
        await this.aiChatService.chat(this.dialog);
    }

    constructor() {
    }
}

function delay(ms: number): Promise<void> {
    return new Promise<void>(resolve => setTimeout(resolve, ms));
}