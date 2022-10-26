package it.ddbdev.ticketsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.ddbdev.ticketsystem.entity.Category;
import it.ddbdev.ticketsystem.payload.request.CategoryRequest;
import it.ddbdev.ticketsystem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
@SecurityRequirement(name = "ddbdev_ticketsystem")
public class CategoryController {


    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Add category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category successfully added"/*, content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class)) }*/),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public ResponseEntity<String> addCategory(@RequestBody @Valid CategoryRequest categoryRequest){

        Optional<Category> getCategory = categoryService.getCategoryById(categoryRequest.getId());

        if (getCategory.isEmpty()){
            String authorityToPass = categoryRequest.getId().trim().toUpperCase();
            Category category = new Category();
            category.setId(authorityToPass);
            categoryService.addCategory(category);

            return ResponseEntity.ok().body("Category " + category.getId() + " has been added.");
        }

        return ResponseEntity.ok().body("Category " + getCategory.get().getId() + " already present.");
    }

    @GetMapping("/public")
    public ResponseEntity<?> getVisibleCategories(){
        return new ResponseEntity<>(categoryService.getCategoriesJPQL(), HttpStatus.OK);
    }

    @GetMapping("{visible}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllCategories(@PathVariable boolean visible){

        List<Category> categoryList;

        if (visible)
            categoryList = categoryService.getCategoriesJPQL();
        else
            categoryList = categoryService.getAllCategories();

        if (categoryList.isEmpty()){
            return new ResponseEntity<>("No categories found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

}
