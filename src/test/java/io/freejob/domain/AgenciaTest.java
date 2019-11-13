package io.freejob.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.freejob.web.rest.TestUtil;

public class AgenciaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agencia.class);
        Agencia agencia1 = new Agencia();
        agencia1.setId(1L);
        Agencia agencia2 = new Agencia();
        agencia2.setId(agencia1.getId());
        assertThat(agencia1).isEqualTo(agencia2);
        agencia2.setId(2L);
        assertThat(agencia1).isNotEqualTo(agencia2);
        agencia1.setId(null);
        assertThat(agencia1).isNotEqualTo(agencia2);
    }
}
