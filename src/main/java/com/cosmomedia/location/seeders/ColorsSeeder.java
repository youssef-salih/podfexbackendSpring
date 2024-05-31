//package com.cosmomedia.location.seeders;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class ColorsSeeder implements CommandLineRunner {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Transactional
//    @Override
//    public void run(String... args) throws Exception {
//        // Check if the Colors table is empty
//        long count = entityManager.createQuery("SELECT COUNT(c) FROM Colors c", Long.class)
//                .getSingleResult();
//
//        if (count == 0) {
//            List<Colors> colorsList = Arrays.asList(
//                    new Colors("Red", "#FF0000"),
//                    new Colors("Green", "#00FF00"),
//                    new Colors("Blue", "#0000FF"),
//                    new Colors("Yellow", "#FFFF00"),
//                    new Colors("Cyan", "#00FFFF"),
//                    new Colors("Magenta", "#FF00FF"),
//                    new Colors("White", "#FFFFFF"),
//                    new Colors("Black", "#000000"),
//                    new Colors("Gray", "#808080"),
//                    new Colors("Dark Red", "#8B0000"),
//                    new Colors("Dark Green", "#006400"),
//                    new Colors("Dark Blue", "#00008B"),
//                    new Colors("Light Yellow", "#FFFFE0"),
//                    new Colors("Light Cyan", "#E0FFFF"),
//                    new Colors("Light Magenta", "#FFB6C1"),
//                    new Colors("Light Gray", "#D3D3D3"),
//                    new Colors("Dark Gray", "#A9A9A9"),
//                    new Colors("Maroon", "#800000"),
//                    new Colors("Olive", "#808000"),
//                    new Colors("Navy", "#000080"),
//                    new Colors("Purple", "#800080"),
//                    new Colors("Teal", "#008080"),
//                    new Colors("Silver", "#C0C0C0"),
//                    new Colors("Brown", "#A52A2A"),
//                    new Colors("Dark Olive Green", "#556B2F"),
//                    new Colors("Dark Slate Gray", "#2F4F4F"),
//                    new Colors("Indigo", "#4B0082"),
//                    new Colors("Dark Turquoise", "#00CED1"),
//                    new Colors("Dark Violet", "#9400D3"),
//                    new Colors("Light Coral", "#F08080"),
//                    new Colors("Light Sea Green", "#20B2AA"),
//                    new Colors("Light Sky Blue", "#87CEFA"),
//                    new Colors("Light Slate Gray", "#778899"),
//                    new Colors("Light Steel Blue", "#B0C4DE"),
//                    new Colors("Pale Green", "#98FB98"),
//                    new Colors("Pale Turquoise", "#AFEEEE"),
//                    new Colors("Pale Violet Red", "#DB7093"),
//                    new Colors("Cornflower Blue", "#6495ED"),
//                    new Colors("Hot Pink", "#FF69B4"),
//                    new Colors("Dark Salmon", "#E9967A"),
//                    new Colors("Dark Sea Green", "#8FBC8F"),
//                    new Colors("Dark Orchid", "#9932CC"),
//                    new Colors("Medium Purple", "#9370DB"),
//                    new Colors("Medium Sea Green", "#3CB371"),
//                    new Colors("Medium Slate Blue", "#7B68EE"),
//                    new Colors("Medium Spring Green", "#00FA9A"),
//                    new Colors("Medium Turquoise", "#48D1CC"),
//                    new Colors("Dark Khaki", "#BDB76B"),
//                    new Colors("Dark Goldenrod", "#B8860B"),
//                    new Colors("Dark Slate Blue", "#483D8B"),
//                    new Colors("Firebrick", "#B22222"),
//                    new Colors("Forest Green", "#228B22"),
//                    new Colors("Dodger Blue", "#1E90FF"),
//                    new Colors("Gold", "#FFD700"),
//                    new Colors("Deep Pink", "#FF1493"),
//                    new Colors("Dark Orange", "#FF8C00"),
//                    new Colors("Dark Cyan", "#008B8B"),
//                    new Colors("Dark Magenta", "#8B008B"),
//                    new Colors("Olive Drab", "#6B8E23"),
//                    new Colors("Medium Violet Red", "#C71585"),
//                    new Colors("Medium Aquamarine", "#66CDAA"),
//                    new Colors("Medium Orchid", "#BA55D3"),
//                    new Colors("Medium Blue", "#0000CD"),
//                    new Colors("Medium Forest Green", "#6B8E23"),
//                    new Colors("Medium Goldenrod", "#EEDD82"),
//                    new Colors("Midnight Blue", "#191970"),
//                    new Colors("Pale Goldenrod", "#EEE8AA"),
//                    new Colors("Pale Green", "#98FB98"),
//                    new Colors("Pale Turquoise", "#AFEEEE"),
//                    new Colors("Pale Violet Red", "#DB7093"),
//                    new Colors("Papaya Whip", "#FFEFD5"),
//                    new Colors("Peach Puff", "#FFDAB9"),
//                    new Colors("Peru", "#CD853F"),
//                    new Colors("Pink", "#FFC0CB"),
//                    new Colors("Plum", "#DDA0DD"),
//                    new Colors("Powder Blue", "#B0E0E6"),
//                    new Colors("Rosy Brown", "#BC8F8F"),
//                    new Colors("Royal Blue", "#4169E1"),
//                    new Colors("Saddle Brown", "#8B4513"),
//                    new Colors("Salmon", "#FA8072"),
//                    new Colors("Sandy Brown", "#F4A460"),
//                    new Colors("Sea Green", "#2E8B57"),
//                    new Colors("Sea Shell", "#FFF5EE"),
//                    new Colors("Sienna", "#A0522D"),
//                    new Colors("Sky Blue", "#87CEEB"),
//                    new Colors("Slate Blue", "#6A5ACD"),
//                    new Colors("Slate Gray", "#708090"),
//                    new Colors("Snow", "#FFFAFA"),
//                    new Colors("Spring Green", "#00FF7F"),
//                    new Colors("Steel Blue", "#4682B4"),
//                    new Colors("Tan", "#D2B48C"),
//                    new Colors("Thistle", "#D8BFD8"),
//                    new Colors("Tomato", "#FF6347")
//            );
//
//            colorsList.forEach(color -> entityManager.persist(color));
//        }
//    }
//}
