package Service;
import java.util.List;
import java.util.ArrayList;
import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;
import Model.Account;

public class MessageService {
    public MessageDAO messageDAO;
    public AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message addMessage(Message message) {
        if (message.getMessage_text().length() <= 0) {
            return null;
        }

        if (message.getMessage_text().length() >= 255) {
            return null;
        }

        Account account = accountDAO.getAccountById(message.getPosted_by());

        if (account == null) {
            return null;
        }

        return messageDAO.insertMessage(message);
    }

    public Message updateMessage(int messageId, String messageText) {
        if (messageText.length() <= 0) {
            return null;
        }

        if (messageText.length() >= 255) {
            return null;
        }

        Message fetchedMessage = messageDAO.getMessageById(messageId);
        if (fetchedMessage != null) {
            boolean messageUpdated = messageDAO.updateMessage(messageId, messageText);
            if (messageUpdated) {
                fetchedMessage.setMessage_text(messageText);
                return fetchedMessage;
            }
        }

        return null;
    }

    public Message deleteMessageById(int messageId) {
        Message fetchedMessage = messageDAO.getMessageById(messageId);
        if (fetchedMessage != null) {
            messageDAO.deleteMessageById(messageId);
            return fetchedMessage;
        }

        return null;
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        Account fetchedAccount = accountDAO.getAccountById(accountId);
        if (fetchedAccount != null) {
            return messageDAO.getMessagesByAccountId(accountId);
        }

        return new ArrayList<Message>();
    }
}
