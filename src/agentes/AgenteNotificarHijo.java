package agentes;


import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AgenteNotificarHijo extends Agent {
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println("AgenteNotificacionHijo recibió el mensaje: " + msg.getContent());
                    doDelete(); // Termina el agente después de recibir el mensaje
                } else {
                    block();
                }
            }
        });
    }
}
