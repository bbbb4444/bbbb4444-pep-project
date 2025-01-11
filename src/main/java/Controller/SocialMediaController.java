package Controller;

import java.util.List;
import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Service.MessageService;
import Service.AccountService;
import Model.Message;
import Model.Account;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageService messageService;
    AccountService accountService;

    public SocialMediaController() {
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::registerHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::createMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageHandler);
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        app.patch("messages/{message_id}", this::updateMessageHandler);
        app.get("accounts/{account_id}/messages", this::getMessagesByAccountHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount != null) {
            context.json(mapper.writeValueAsString(addedAccount)).status(200);
        } else {
            context.status(400);
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loggedInAccount = accountService.login(account);
        if (loggedInAccount != null) {
            context.json(mapper.writeValueAsString(loggedInAccount)).status(200);
        } else {
            context.status(401);
         }

    }

    private void createMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage != null) {
            context.json(mapper.writeValueAsString(addedMessage)).status(200);
        } else {
            context.status(400);
        }
    }

    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages).status(200);
    }

    private void getMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            context.json(message).status(200);;
        } else {
            context.status(200);
        }
    }

    private void deleteMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.deleteMessageById(messageId);

        if (message != null) {
            context.json(message).status(200);
        } else {
            context.status(200);
        }
    }

    private void updateMessageHandler(Context context) throws JsonProcessingException {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        String messageText = message.getMessage_text();

        Message updatedMessage = messageService.updateMessage(messageId, messageText);
        if (updatedMessage != null) {
            context.json(mapper.writeValueAsString(updatedMessage)).status(200);
        } else {
            context.status(400);
        }
    }

    private void getMessagesByAccountHandler(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        context.json(messages).status(200);
    }
}