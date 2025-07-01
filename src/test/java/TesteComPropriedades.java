import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.FloatRange;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;
import org.example.simulationV1.cluster.Cluster;
import org.example.simulationV1.criatura.Criatura;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.simulationV1.simulation.ProcessamentoCriaturas.gerarCriaturas;

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

    @Property
    public void quandoClusterSeAproximarDeCriaturaRoubarMetadeDeSuasMoedas(
            @Size(2)
            @ForAll
            List<@IntRange(min= 0, max = 100000) Integer> quantidadeMoedasCriatura
    ){
        Criatura criatura1 = new Criatura();
        Criatura criatura2 = new Criatura();

        criatura1.setMoedas(quantidadeMoedasCriatura.get(0));
        criatura2.setMoedas(quantidadeMoedasCriatura.get(1));

        Cluster cluster = new Cluster();

        criatura1.cluster = cluster;
        criatura1.getCluster().setMoedasDoCluster(criatura1.getMoedas());
        criatura1.getCluster().receiveCoins(criatura2.giveCoins());
        assertThat(criatura1.getCluster().getMoedasDoCluster()).isEqualTo(
                quantidadeMoedasCriatura.get(0)+quantidadeMoedasCriatura.get(1)/2
        );
    }

    @Property
    public void clusterPodeSerFormadoPorUmaOuMaisCriaturas(
            @ForAll
            @IntRange(min=2, max=100) Integer quantidadeDeCriaturas
    ){
        Criatura[] criaturas = new Criatura[quantidadeDeCriaturas];
        Cluster cluster = new Cluster();

        criaturas[0] = new Criatura();
        criaturas[0].cluster = cluster;
        for(int i=1; i<quantidadeDeCriaturas; i++){
            criaturas[i] = new Criatura();
            criaturas[0].getCluster().addCriatura(criaturas[i]);
        }

        assertThat(criaturas[0].getCluster().getTotalDeCriaturas()).isEqualTo(quantidadeDeCriaturas);
    }

    @Property
    public void quantidadeInicialDeMoedasDaCriaturaGuardiaoDeveSerZero(
            @ForAll
            @IntRange(min=2, max=200) Integer quantidadeDeCriaturas
    ){
        Criatura[] criaturas = gerarCriaturas(quantidadeDeCriaturas,0.1);
        assertThat(criaturas[criaturas.length-1].getMoedas()).isEqualTo(0);
    }

    @Property
    public void guardiaoRoubaTodasAsMoedasDeUmCluster(
            @Size(2)
            @ForAll
            List<@IntRange(min= 0, max = 100000) Integer> quantidadeMoedasCriatura
    ){
        Criatura criatura1 = new Criatura();
        Criatura criatura2 = new Criatura();

        criatura1.setMoedas(quantidadeMoedasCriatura.get(0));
        criatura2.setMoedas(quantidadeMoedasCriatura.get(1));

        Cluster cluster = new Cluster();

        criatura1.cluster = cluster;
        criatura2.guardiao = true;
        criatura1.getCluster().setMoedasDoCluster(criatura1.getMoedas());

        criatura2.receiveCoins(criatura1.getCluster().giveCoins(criatura2.guardiao));
        assertThat(criatura2.getMoedas()).isEqualTo(
                quantidadeMoedasCriatura.get(0)+quantidadeMoedasCriatura.get(1)
        );
    }

    //guardiaoSeMoveAposTodasAsOutrasCriaturas
    //simulacaoBemSucedidaQuandoSobraUmaCriaturaEoGuardiao
}
