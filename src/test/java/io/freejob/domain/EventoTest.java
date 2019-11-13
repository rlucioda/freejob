package io.freejob.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.freejob.web.rest.TestUtil;

public class EventoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evento.class);
        Evento evento1 = new Evento();
        evento1.setId(1L);
        Evento evento2 = new Evento();
        evento2.setId(evento1.getId());
        assertThat(evento1).isEqualTo(evento2);
        evento2.setId(2L);
        assertThat(evento1).isNotEqualTo(evento2);
        evento1.setId(null);
        assertThat(evento1).isNotEqualTo(evento2);
    }
}
