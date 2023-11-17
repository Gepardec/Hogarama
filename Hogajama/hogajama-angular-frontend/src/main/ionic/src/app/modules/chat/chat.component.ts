import {Component, ElementRef, EventEmitter, Output, ViewChild} from '@angular/core';
import {ChatService, Message} from 'src/app/services/ChatService/chat.service';
import {HogaramaBackendService} from "../../services/HogaramaBackendService/hogarama-backend.service";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent {

  @ViewChild('textareaRef', { read: ElementRef }) textareaElement: ElementRef | undefined;

  userMessage = '';
  progress = false;
  messages: Message[] = [];

  @Output() newMessage = new EventEmitter<string>();

  constructor(private chat: ChatService, private backend: HogaramaBackendService) {
    this.messages = chat.dialog.messages;
  }


  async onKeydown(event: KeyboardEvent) {
    if (event.ctrlKey && event.key === 'Enter') {
      this.sendMessage();
      // Add your custom logic here
    }
  }

  async sendMessage() {
    if(this.userMessage === '') {
      return;
    }
    this.progress = true;
    this.newMessage.emit(this.userMessage);
    const msg = this.userMessage;
    this.userMessage = ''
    await this.chat.say(msg);
    this.progress = false;
    console.log(this.textareaElement)
    if(this.textareaElement) {
      this.textareaElement.nativeElement.focus();
    }
  }

  async clearMessages() {
    this.chat.dialog.clearMessages();
  }

  abort(message: Message) {
    console.log('Abort. Original JSON was ' + message.action);
    message.applyAbort();
  }

  async confirm(message: Message) {
    console.log('Confirm. Original JSON was ' + JSON.stringify(message.action));
    if(message.action.operation === 'change_rule') {
        const rule = message.action.dto;
        if(rule) {
            try {
              const actionResult = await this.backend.rules.patch(rule.id, rule);
              console.log(actionResult);
              message.applyConfirm();
              return;
            } catch (e) {
              message.error('Something went wrong during confirmation.');
            }

        }
    }
  }

  protected readonly JSON = JSON;
}