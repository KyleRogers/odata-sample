package net.cyphoria.samples.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

/**
 * @author Stefan Pennndorf <stefan@cyphoria.net>
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue
    private Long id;

    private Tenant tenant;

    private String orderNumber;

    private BigDecimal orderValue;

    private Currency currency;

    @OneToMany
    private List<OrderLineItem> orderLineItems;


}
