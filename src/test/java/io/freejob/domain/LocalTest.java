package io.freejob.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.freejob.web.rest.TestUtil;

public class LocalTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Local.class);
        Local local1 = new Local();
        local1.setId(1L);
        Local local2 = new Local();
        local2.setId(local1.getId());
        assertThat(local1).isEqualTo(local2);
        local2.setId(2L);
        assertThat(local1).isNotEqualTo(local2);
        local1.setId(null);
        assertThat(local1).isNotEqualTo(local2);
    }
}
