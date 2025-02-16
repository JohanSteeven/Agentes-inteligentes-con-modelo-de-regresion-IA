package agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AgenteNotificar extends Agent {
    protected void setup() {
        addBehaviour(new MostrarPrediccionBehaviour());
    }

    private class MostrarPrediccionBehaviour extends CyclicBehaviour {
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                System.out.println("Predicción recibida: " + msg.getContent());

                // Enviar mensaje a AgenteANN para que cree AgenteNotificacionHijo
                ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                reply.addReceiver(getAID("AgenteANN"));
                reply.setContent("crear hijo");
                send(reply);

                // Mostrar confirmación
                System.out.println("Mensaje enviado a AgenteANN para crear AgenteNotificacionHijo.");
                System.out.println(getLocalName() + " ha terminado.");

                // Terminar el agente después de mostrar la predicción y enviar el mensaje
                doDelete();
            } else {
                block();
            }
        }
    }

    @Override
    protected void takeDown() {
        System.out.println(getLocalName() + " ha terminado.");
    }
}
