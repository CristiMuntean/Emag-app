package com.emag.logic;

import com.emag.models.Cart;
import com.emag.models.CartItem;
import com.emag.models.Product;
import com.emag.models.User;
import com.emag.services.CartService;
import com.emag.services.ProductService;
import com.emag.services.UserService;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Service
public class HtmlHandler {
    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;


    public String handleCartHtml() {
        try {
            String text = new String(Files.readAllBytes(Path.of("src/main/resources/templates/cart.html")), StandardCharsets.UTF_8);
            Document document = Jsoup.parse(text);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)) {
                String currentUserName = authentication.getName();

                document.select("button#loginButton").get(0).remove();
                document.select("button#registerButton").get(0).remove();
                double totalPrice = 0;
                User currentUser = userService.getUserDetails(authentication.getName());
                Cart cart = cartService.getCartForUser(currentUser);
                Elements elements = document.select("div.card-body").get(0).select("div.row");
                elements.get(0).remove();
                for (CartItem cartItem: cart.getCartItems()) {
                    Element cartItemDocument = elements.get(0).clone();
                    Product product = productService.getProductWithName(cartItem.getItemName());
                    Elements dataParagraphs = cartItemDocument.select("div#data").get(0).select("input");
                    dataParagraphs.get(0).attr("placeholder",cartItem.getUserEmail());
                    dataParagraphs.get(1).attr("placeholder",cartItem.getItemName());
                    cartItemDocument.select("div#data").select("p").get(1).text("Description: " + product.getProductDescription());
                    cartItemDocument.select("div#data").get(0).removeAttr("id");
                    Elements nrParagraphs = cartItemDocument.select("div#nr").get(0).select("input");
                    nrParagraphs.get(0).attr("placeholder",cartItem.getItemQuantity());
                    double price = Integer.parseInt(cartItem.getItemQuantity()) * Double.parseDouble(product.getProductPrice());
                    totalPrice += price;
                    cartItemDocument.select("div#nr").get(0).select("p").get(1).text("Price: $" + price);
                    cartItemDocument.select("div#nr").get(0).removeAttr("id");
                    document.select("div.card-body").get(0).append(cartItemDocument.toString());
                    document.select("div.card-body").get(0).append("<hr class=\"my-4\" />");
                }
                Elements prices = document.select("div.col-md-4").get(0).select("div.card").get(0).select("div.card-body").get(0).select("ul").get(0).select("li");
                prices.get(0).select("span").get(0).text("$"+totalPrice);
                prices.get(2).select("span").get(0).text("$"+totalPrice);
                if(cart.getCartItems().size()==0){
                    Element ul = document.select("ul.list-group").get(0);
                    ul.select("li").get(0).remove();
                    ul.select("li").get(0).remove();
                    ul.select("li").get(0).select("div").get(0).select("strong").get(0).text("The shopping cart");
                    ul.select("li").get(0).select("div").get(0).select("strong").get(1).select("p").get(0).text("is empty");
                    ul.select("li").get(0).select("span").get(0).remove();
                    document.select("button#checkoutButton").get(0).attr("style","display: none;");
                }
            }
            File indexFile = new File("src/main/resources/templates/newCart.html");
            FileUtils.writeStringToFile(indexFile, document.toString(), StandardCharsets.UTF_8);

            return "newCart";

        } catch (IOException | ExecutionException | InterruptedException e) {
            System.out.println("Problem while displaying products");
            return "cart";
        }
    }

    public String handleSearchHtml(String name) {
        try {
            String text = new String(Files.readAllBytes(Path.of("src/main/resources/templates/index.html")),StandardCharsets.UTF_8);
            Document document = Jsoup.parse(text);
            Elements elements = document.select("div.card");
            elements.get(0).remove();

            ArrayList<Product> products = productService.getProductsWithContainFilter(name);
            for (Product product: products) {
                Element productDocument = elements.get(0).clone();
                productDocument.select("div.card-body").get(0).select("input").get(0).attr("placeholder",product.getProductName());
                productDocument.select("div.card-body").get(0).select("input").get(0).attr("value",product.getProductName());
                productDocument.select("div.card-body").get(0).select("input").get(1).attr("placeholder",product.getProductDescription());
                productDocument.select("div.card-body").get(0).select("input").get(1).attr("value",product.getProductDescription());
                productDocument.select("ul.list-group").get(0).select("li").get(0).select("input").get(0).attr("placeholder", product.getProductPrice());
                productDocument.select("ul.list-group").get(0).select("li").get(0).select("input").get(0).attr("value", product.getProductPrice());

                productDocument.addClass("d-inline-block");
                productDocument.attr("style","width: 18rem;");
                document.select("body").append(productDocument.toString());
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)) {
                String currentUserName = authentication.getName();
                System.out.println(currentUserName);

                document.select("button#loginButton").get(0).remove();
                document.select("button#registerButton").get(0).remove();
            }
            else {
                document.select("button#logoutButton").get(0).remove();
            }
            File indexFile = new File("src/main/resources/templates/newIndex.html");
            FileUtils.writeStringToFile(indexFile, document.toString(), StandardCharsets.UTF_8);
            String newText = new String(Files.readAllBytes(Path.of("src/main/resources/templates/newIndex.html")),StandardCharsets.UTF_8);
            Document newDocument = Jsoup.parse(newText);

            return "newIndex";
        } catch (IOException e) {
            System.out.println("Problem while displaying products");
            return "index";
        }
    }

    public String handleAddToCart(Model model, Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            try {
                User currentUser = userService.getUserDetails(currentUserName);
                if(cartService.containsItemInCartForUser(currentUser,product)!=null){
                    cartService.increaseCartItemQuantity(currentUser,product);
                }
                else {
                    cartService.createCartItem(new CartItem(currentUserName, product.getProductName(), "1"));
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return handleIndexHtml(model);
    }

    public String handleIndexHtml(Model model) {
        ArrayList<User> users = userService.getUsers();
        ArrayList<UserDetails> userDetails = new ArrayList<>();
        for (User user: users){
            UserDetails userDetailsAux = org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                    .password(NoOpPasswordEncoder.getInstance().encode(user.getPassword()))
                    .roles("USER").build();
            userDetails.add(userDetailsAux);
            if (!inMemoryUserDetailsManager.userExists(user.getEmail())) {
                inMemoryUserDetailsManager.createUser(userDetailsAux);
            }
        }
        try {
            String text = new String(Files.readAllBytes(Path.of("src/main/resources/templates/index.html")),StandardCharsets.UTF_8);
            Document document = Jsoup.parse(text);
            Elements elements = document.select("div.card");
            elements.get(0).remove();

            ArrayList<Product> products = productService.getProducts();
            for (Product product: products) {
                Element productDocument = elements.get(0).clone();
                productDocument.select("div.card-body").get(0).select("input").get(0).attr("placeholder",product.getProductName());
                productDocument.select("div.card-body").get(0).select("input").get(0).attr("value",product.getProductName());
                productDocument.select("div.card-body").get(0).select("input").get(1).attr("placeholder",product.getProductDescription());
                productDocument.select("div.card-body").get(0).select("input").get(1).attr("value",product.getProductDescription());
                productDocument.select("ul.list-group").get(0).select("li").get(0).select("input").get(0).attr("placeholder", product.getProductPrice());
                productDocument.select("ul.list-group").get(0).select("li").get(0).select("input").get(0).attr("value", product.getProductPrice());

                productDocument.addClass("d-inline-block");
                productDocument.attr("style","width: 18rem;");
                document.select("body").append(productDocument.toString());
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)) {
                document.select("button#loginButton").get(0).remove();
                document.select("button#registerButton").get(0).remove();
            }
            else {
                document.select("button#logoutButton").get(0).remove();
            }

            File indexFile = new File("src/main/resources/templates/newIndex.html");
            FileUtils.writeStringToFile(indexFile, document.toString(), StandardCharsets.UTF_8);
            String newText = new String(Files.readAllBytes(Path.of("src/main/resources/templates/newIndex.html")),StandardCharsets.UTF_8);
            Document newDocument = Jsoup.parse(newText);

            return "newIndex";
        } catch (IOException e) {
            System.out.println("Problem while displaying products");
            return "index";
        }
    }
}
