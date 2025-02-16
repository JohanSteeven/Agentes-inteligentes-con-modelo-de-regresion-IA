package agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgenteANN extends Agent {
    protected void setup() {
        addBehaviour(new PrediccionBehaviour());
        addBehaviour(new CrearHijoBehaviour());
    }

    private class PrediccionBehaviour extends CyclicBehaviour {
        public void action() {
            ACLMessage msg = receive();
            if (msg != null && msg.getPerformative() == ACLMessage.INFORM && !msg.getContent().equals("crear hijo")) {
                String inputData = msg.getContent();
                String[] data = inputData.split(",");

                if (data.length != 9) {
                    System.out.println("Error: Se esperaban 9 argumentos de entrada, pero se recibieron " + data.length);
                    return;
                }

                try {
                    // Construir el comando para ejecutar el script de Python
                    String[] command = new String[data.length + 2];
                    command[0] = "python";
                    command[1] = "src/python/Regresion.py";
                    System.arraycopy(data, 0, command, 2, data.length);

                    // Ejecutar el script de Python
                    ProcessBuilder pb = new ProcessBuilder(command);

                    // Establecer variables de entorno para TensorFlow
                    Map<String, String> env = pb.environment();
                    env.put("TF_CPP_MIN_LOG_LEVEL", "3");
                    env.put("TF_ENABLE_ONEDNN_OPTS", "0");

                    Process process = pb.start();

                    // Leer la salida del script de Python
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String result;
                    while ((result = in.readLine()) != null) {
                        System.out.println("Predicción del modelo: " + result);
                        ACLMessage resultadoMsg = new ACLMessage(ACLMessage.INFORM);
                        resultadoMsg.addReceiver(getAID("AgenteNotificar"));
                        resultadoMsg.setContent(result);
                        send(resultadoMsg);
                    }

                    // Leer errores del script de Python
                    BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String errorLine;
                    while ((errorLine = err.readLine()) != null) {
                        System.err.println("Error: " + errorLine);
                    }

                    // Asegúrate de esperar a que el proceso termine
                    int exitCode = process.waitFor();
                    if (exitCode != 0) {
                        System.err.println("Script Python terminó con el código de salida " + exitCode);
                    }

                } catch (Exception e) {
                    Logger.getLogger(AgenteANN.class.getName()).log(Level.SEVERE, "Error en la ejecución del script Python", e);
                }
            } else {
                block();
            }
        }
    }

    private class CrearHijoBehaviour extends CyclicBehaviour {
        public void action() {
            ACLMessage msg = receive();
            if (msg != null && msg.getContent().equals("crear hijo")) {
                try {
                    AgentContainer container = getContainerController();
                    AgentController newAgent = container.createNewAgent("AgenteNotificarHijo", "agentes.AgenteNotificarHijo", null);
                    newAgent.start();
                } catch (StaleProxyException e) {
                    Logger.getLogger(AgenteANN.class.getName()).log(Level.SEVERE, "Error al crear el nuevo agente", e);
                }
            } else {
                block();
            }
        }
    }
}
