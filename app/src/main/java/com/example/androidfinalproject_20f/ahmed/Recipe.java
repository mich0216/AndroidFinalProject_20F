package com.example.androidfinalproject_20f.ahmed;

public class Recipe {

    /**
     * database ID
     */
    private long id;
    /**
     * recipe name
     */
    private String title;
    /**
     * Link of the recipe
     */
    private String recipeUrl;
    /**
     * Recipe's ingredients
     */
    private String ingredients;
    /**
     * thumbnail of the recipe
     */
    private String imageUrl;

    public Recipe(String title, String recipeUrl, String ingredients, String imageUrl) {
        this.title = title;
        this.recipeUrl = recipeUrl;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
