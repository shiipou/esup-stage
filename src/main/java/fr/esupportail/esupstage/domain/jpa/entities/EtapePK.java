package fr.esupportail.esupstage.domain.jpa.entities;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The primary key class for the Etape database table.
 *
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class EtapePK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;
    @Column(nullable = false, length = 10)
    private String codeEtape;
    @Column(nullable = false, length = 10)
    private String codeVersionEtape;
    @Column(nullable = false, length = 50)
    private String codeUniversite;

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EtapePK)) {
            return false;
        }
        EtapePK castOther = (EtapePK) other;
        return this.codeEtape.equals(castOther.codeEtape) && this.codeVersionEtape.equals(castOther.codeVersionEtape)
                && this.codeUniversite.equals(castOther.codeUniversite);
    }

    public int hashCode() {
        final Integer prime = 31;
        Integer hash = 17;
        hash = hash * prime + this.codeEtape.hashCode();
        hash = hash * prime + this.codeVersionEtape.hashCode();
        hash = hash * prime + this.codeUniversite.hashCode();
        return hash;
    }
}