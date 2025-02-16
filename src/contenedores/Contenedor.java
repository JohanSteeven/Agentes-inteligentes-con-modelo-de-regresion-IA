package contenedores;

import agentes.AgenteDatos;
import agentes.AgenteNotificarHijo;
import agentes.AgenteNotificar;
import agentes.AgenteANN;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;


import java.util.logging.Level;
import java.util.logging.Logger;

public class Contenedor {
    private AgentContainer agentContainer;

    public void crearContenedor(){

        jade.core.Runtime runtime = jade.core.Runtime.instance(); // Esto es un proceso como tal para el contenedor
        Profile profile = new ProfileImpl(null, 1099, null); // null : escoge aleatorio
        runtime.createMainContainer(profile);
        agentContainer = runtime.createAgentContainer(profile);
        agregarAgentes();


    }

    private void agregarAgentes()  { // try catch

        try{
            agentContainer.createNewAgent("AgNotificarHijo", AgenteNotificarHijo.class.getName(), new Object[]{this,1}).start();
            agentContainer.createNewAgent("AgNotificar", AgenteNotificar.class.getName(), new Object[]{this,1}).start();
            agentContainer.createNewAgent("AgenteANN", AgenteANN.class.getName(), new Object[]{this,1}).start();
            agentContainer.createNewAgent("AgDatos", AgenteDatos.class.getName(), new Object[]{this,1}).start();



        }catch(StaleProxyException ex){
            Logger.getLogger(Contenedor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
