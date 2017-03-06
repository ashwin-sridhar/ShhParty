package de.tudarmstadt.informatik.tk.shhparty.chat;

/**
 * Created by Preneesh on 05-01-2017.
 */

import java.io.Serializable;
import java.util.Random;

public class ChatMessage implements Serializable {

    public String body, sender, receiver, senderName;
    public String Date, Time;
    public String msgid;
    public boolean isMine;// Did I send the message.

    public ChatMessage(String Sender, String Receiver, String messageString,
                       String ID, boolean isMINE) {
        body = messageString;
        isMine = isMINE;
        sender = Sender;
        msgid = ID;
        receiver = Receiver;
        senderName = sender;
    }

    public void setMsgID() {

        msgid += "-" + String.format("%02d", new Random().nextInt(100));

    }
}