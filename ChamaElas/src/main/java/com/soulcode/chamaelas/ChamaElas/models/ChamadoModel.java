package com.soulcode.chamaelas.ChamaElas.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tickets")
public class ChamadoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ticket_id")
    private Long ticketId;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String department;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updatedTimestamp;

    @ManyToOne
    private ClienteModel client;

    @ManyToOne
    private TecnicoModel technician;

    private TicketStatus status;

    private Priority priority;

    public enum Priority {
        AGUARDANDO,
        BAIXA,
        MEDIA,
        ALTA;
    }

    @Getter
    public enum TicketStatus {
        ABERTO("Aguardando técnico"),
        EM_ANDAMENTO("Em atendimento"),
        ENCAMINHADO("Escalado para outro setor"),
        FECHADO("Finalizado");

        private final String description;

        TicketStatus(String description) {
            this.description = description;
        }

    }


}