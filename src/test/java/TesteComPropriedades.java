import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.FloatRange;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;
import org.example.simulationV1.cluster.Cluster;
import org.example.simulationV1.criatura.Criatura;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TesteComPropriedades{
    @Property
    public void juntarQuantidadeDeMoedasDeCriaturasAoSeTornaremUmCluster(
            @Size(2)
            @ForAll
            List<@IntRange(min= 0, max = 100000) Integer> quantidadeMoedasCriatura
    ){
        Criatura criatura1 = new Criatura();
        Criatura criatura2 = new Criatura();

        criatura1.setMoedas(quantidadeMoedasCriatura.get(0));
        criatura2.setMoedas(quantidadeMoedasCriatura.get(1));

        Cluster cluster = new Cluster();
        cluster.setMoedasDoCluster(criatura1.getMoedas()+criatura2.getMoedas());

        assertThat(cluster.getMoedasDoCluster()).isEqualTo(
                quantidadeMoedasCriatura.get(0)+quantidadeMoedasCriatura.get(1)
        );
    }
}
