package com.emag.controller;
import com.emag.logic.HtmlHandler;
import com.emag.models.CartItem;
import com.emag.models.Product;
import com.emag.models.User;
import com.emag.services.CartService;
import com.emag.services.ProductService;
import com.emag.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ExecutionException;


@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private HtmlHandler handler;

    @GetMapping("/")
    public String welcome(Model model) {
        model.addAttribute("product", new Product());
        return handler.handleIndexHtml(model);
    }

    @GetMapping("/login")
    public String login() {
        return "reidrect:/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user",new User());
        return "register";
    }

    @PostMapping("/postUser")
    public String registerSubmit(@ModelAttribute User user, Model model) {
        model.addAttribute("user", user);
        try {
            userService.saveUserDetails(user);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            System.out.println("Cannot create a new user");
        }
        return welcome(model);
    }

    @PostMapping("/addToCart")
    public String addToCart(@ModelAttribute Product product, Model model) {
        model.addAttribute("product", product);
        return handler.handleAddToCart(model,product);
    }

    @PostMapping("/search")
    public String searchProducts(@RequestParam("name_filter")String name, Model model) {
        model.addAttribute("product",new Product());
        return handler.handleSearchHtml(name);
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        model.addAttribute("cartItem", new CartItem());
        return handler.handleCartHtml();
    }

    @PostMapping("/removeItem")
    public String removeCartItem(@ModelAttribute CartItem cartItem, Model model) {
        model.addAttribute("cartItem", cartItem);
        cartService.deleteItem(cartItem);
        return cart(model);
    }

}