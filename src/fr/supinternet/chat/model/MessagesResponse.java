package fr.supinternet.chat.model;

import java.util.List;

public class MessagesResponse extends Response{
	
	private List<Message> messages;

	public MessagesResponse() {
	}

	public MessagesResponse(String status, ResponseCode code) {
		super(status, code);
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

}

