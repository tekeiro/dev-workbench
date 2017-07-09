package de.roamingthings.devworkbench.category.service

import de.roamingthings.devworkbench.category.domain.Category
import de.roamingthings.devworkbench.category.repository.CategoryRepository
import de.roamingthings.devworkbench.link.api.CategoryDto
import de.roamingthings.devworkbench.link.api.CreateCategoryDto
import de.roamingthings.devworkbench.link.api.UpdateCategoryDto
import org.slf4j.Logger
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/07/08
 */
interface CategoryService {
    fun createCategory(createRequest: CreateCategoryDto): CategoryDto
    fun updateCategory(id: Long, updateRequest: UpdateCategoryDto): CategoryDto?
    fun deleteCategory(id: Long)
    fun retrieveAllCategories(): List<CategoryDto>
    fun retrieveCategory(id: Long): CategoryDto?
    fun retrieveCategoryByCode(code: String): CategoryDto?
}

@Service
@Transactional
internal class CategoryServiceImpl(val categoryRepository: CategoryRepository, val log: Logger) : CategoryService {
    override fun createCategory(createRequest: CreateCategoryDto): CategoryDto {
        val createdCategory = categoryRepository.save(Category.fromDto(createRequest))
        return createdCategory.toDto()
    }

    override fun updateCategory(id: Long, updateRequest: UpdateCategoryDto): CategoryDto? {
        val currentCategory = categoryRepository.findOne(id)

        return if (currentCategory != null) {
            categoryRepository.save(currentCategory.updateFromDto(updateRequest)).toDto()
        } else {
            null
        }
    }

    override fun deleteCategory(id: Long) {
        categoryRepository.delete(id)
    }

    override fun retrieveAllCategories(): List<CategoryDto> {
        return categoryRepository.findAll().map { it.toDto() }
    }

    override fun retrieveCategory(id: Long): CategoryDto? {
        return categoryRepository.findOne(id)?.toDto()
    }

    override fun retrieveCategoryByCode(code: String): CategoryDto? {
        return categoryRepository.findOneByCode(code)?.toDto()
    }
}