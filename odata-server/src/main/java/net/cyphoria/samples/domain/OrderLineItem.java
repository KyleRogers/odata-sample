package net.cyphoria.samples.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Stefan Pennndorf <stefan@cyphoria.net>
 */
@Entity
@Table(name = "lineitems")
public class OrderLineItem {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    private String productName;

    private Integer quantity;

    private BigDecimal lineItemValue;

    private BigDecimal vatAmount;

    private BigDecimal vatPercent;
}
