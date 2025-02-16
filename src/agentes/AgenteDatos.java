package agentes;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import javax.swing.JOptionPane;

public class AgenteDatos extends Agent {

    @Override
    protected void setup() {
        addBehaviour(new Comportamiento());
    }

    @Override
    protected void takeDown() {
        System.out.println(getLocalName() + " envío correctamente los datos.");
    }

    class Comportamiento extends OneShotBehaviour {
        private String input;

        @Override
        public void action() {
            boolean validInput = false;

            while (!validInput) {
                // Mostrar un diálogo de entrada para el usuario
                input = JOptionPane.showInputDialog(null,
                        "Ingrese los datos del producto separados por comas:\n" +
                                "Los datos son: Modelo, Marca, Clase, Subgrupo, Cantidad, Precio, Costo Promedio, Costo Total, Año\n"
                                +"(Ejemplo: 1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0)",
                        "Entrada de Datos del Producto",
                        JOptionPane.QUESTION_MESSAGE);

                // Validar la entrada del usuario
                if (input != null && !input.trim().isEmpty()) {
                    String[] parts = input.split(",");
                    if (parts.length == 9) {
                        try {
                            for (String part : parts) {
                                Double.parseDouble(part.trim());
                            }
                            validInput = true;
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null,
                                    "Entrada inválida. Por favor, asegúrese de que todos los valores sean números.",
                                    "Error de Entrada",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Entrada inválida. Por favor, ingrese exactamente 9 valores separados por comas.",
                                "Error de Entrada",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Entrada inválida. La entrada no puede estar vacía.",
                            "Error de Entrada",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            // Enviar mensaje al AgenteANN
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(getAID("AgenteANN"));
            msg.setContent(input);
            send(msg);

            // Terminar el agente
            doDelete();
        }
    }
}
