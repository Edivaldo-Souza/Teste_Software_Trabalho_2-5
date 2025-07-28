package testeSistema;

import org.example.simulationV1.simulation.ProcessamentoCriaturas;

import java.awt.*;
import java.awt.event.KeyEvent;

public class FecharJanelaAutomatico {
    public void fecharJanelaAutomatico(){
        new Thread(()->{
            try {
                Robot robot = new Robot();

                while(true) {
                    if(ProcessamentoCriaturas.isMessageBoxVisible) {
                        Thread.sleep(500);
                        robot.keyPress(KeyEvent.VK_ENTER);
                        robot.keyRelease(KeyEvent.VK_ENTER);
                        break;
                    }
                }
            } catch (AWTException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
