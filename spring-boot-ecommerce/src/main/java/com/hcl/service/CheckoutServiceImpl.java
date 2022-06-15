package com.hcl.service;

import com.hcl.dao.CustomerRepository;
import com.hcl.dto.PaymentInfo;
import com.hcl.dto.Purchase;
import com.hcl.dto.PurchaseResponse;
import com.hcl.entity.Customer;
import com.hcl.entity.Order;
import com.hcl.entity.OrderItem;
import com.hcl.vo.Product;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;
@Slf4j
@Service
public class CheckoutServiceImpl implements CheckoutService {


    private CustomerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;


    public CheckoutServiceImpl(CustomerRepository customerRepository,
                               @Value("${stripe.key.secret}") String secretKey) {
        this.customerRepository = customerRepository;

        // initialize stripe api with secret key
        Stripe.apiKey = secretKey;
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "PureSound Purchase");
        params.put("receipt_email", paymentInfo.getReceiptEmail());
        return PaymentIntent.create(params);
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        // ALbums to be logged for change
        StringBuilder albums = new StringBuilder();
        // retrieve the order info from dto
        Order order = purchase.getOrder();

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        // populate order with billingAddress and shippingAddress
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        // populate customer with order
        Customer customer = purchase.getCustomer();

        // check if this is an existing customer
        String theEmail = customer.getEmail();
        Customer customerFromDB = customerRepository.findByEmail(theEmail);

        if (customerFromDB != null) {
            customer = customerFromDB;
        }

        customer.add(order);

        // save to the database
        customerRepository.save(customer);

        // update product quantity in DB

        for(OrderItem curr: orderItems) {

            String url = "http://CATALOG-SERVICE/api/products/"+curr.getProductId();
            log.info(url);
            Product productUpdate = restTemplate.getForObject
                    (url, Product.class);
            log.info(productUpdate.toString());
            productUpdate.setUnitsInStock(productUpdate.getUnitsInStock() - curr.getQuantity());
            log.info(productUpdate.toString());

            albums.append(productUpdate.getName()+ " | ");

            restTemplate.put(url, productUpdate);
        }


        // Double logging because there is a weird bug that logstash skips the last logged item
        log.info("The following album(s) have been purchased: " + albums);
        log.info("The following album(s) have been purchased: " + albums);

        // return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {

        // generate a random UUID number (UUID version-4)
        // For details see: https://en.wikipedia.org/wiki/Universally_unique_identifier
        //
        return UUID.randomUUID().toString();
    }
}





