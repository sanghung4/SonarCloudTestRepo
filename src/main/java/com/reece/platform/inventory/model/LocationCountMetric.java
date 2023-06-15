package com.reece.platform.inventory.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pi_location_count_metric")
@Getter
@Setter
public class LocationCountMetric extends AuditableEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "completion_status")
    private MetricsCompletionStatus completionStatus;

    @ManyToOne
    @JoinColumn(name = "location_count_id", nullable = false)
    private LocationCount locationCount;

    @Column(name = "time_started")
    private Date timeStarted;
    @Column(name = "time_ended")
    private Date timeEnded;
    @Column(name = "counted")
    private Integer counted;
    @Column(name = "needed_to_be_counted")
    private Integer neededToBeCounted;
    @Column(name = "total")
    private Integer total;
    @Column(name = "error")
    private String error;



}
