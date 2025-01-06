package com.example.ultimate_flashcard_app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ultimate_flashcard_app.dao.TemplateDao
import com.example.ultimate_flashcard_app.data.Entities.Template
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplatesViewModel @Inject constructor(private val templateDao: TemplateDao): ViewModel() {

    val templates: LiveData<List<Template>> = templateDao.getAllTemplates()

    fun getAllTemplates(): LiveData<List<Template>>
    {
        return templateDao.getAllTemplates()
    }

    fun getTemplate(templateId: Int) : LiveData<Template>
    {
        return templateDao.getTemplate(templateId = templateId)
    }

    fun insertTemplate(template: Template)
    {
        viewModelScope.launch {
            templateDao.insertTemplate(template)
        }
    }

    fun deleteTemplate(template: Template)
    {
        viewModelScope.launch {
            templateDao.deleteTemplate(template)
        }
    }

    fun updateTemplate(template: Template)
    {
        viewModelScope.launch {
            templateDao.updateTemplate(template)
        }
    }
}