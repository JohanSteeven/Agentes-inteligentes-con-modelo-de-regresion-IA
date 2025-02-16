package agentes;


import java.io.Serializable;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

public class SendMsg {

    public static void sendMsg(int tipo, String receptor, Agent emisor, String codConversationId, String contenidoString, Serializable contenidoObject, boolean isContentString) {

        ACLMessage acl =  new ACLMessage(tipo);

        AID id =  new AID();
        id.setLocalName(receptor);
        acl.addReceiver(id);

        acl.setSender(emisor.getAID());

        acl.setLanguage(FIPANames.ContentLanguage.FIPA_SL);

        acl.setConversationId(codConversationId);

        if(isContentString) {
            acl.setContent(contenidoString);
        } else {
            try {
                acl.setContentObject(contenidoObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        emisor.send(acl);
    }
}
