package com.main.bitebyte.recipe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeValidationTest {

    @Test
    void newRecipe_defaultServingsIsZero() {
        Recipe recipe = new Recipe();
        assertEquals(0, recipe.getServings());
    }

    @Test
    void newRecipe_isNotPersonalByDefault() {
        Recipe recipe = new Recipe();
        assertTrue(!recipe.isPersonal());
    }
}
