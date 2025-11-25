package com.example.lecom.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * Design System Configuration
 * Centralized place to update theme, fonts, and design tokens
 * 
 * To update the design:
 * 1. Colors: Edit res/values/colors.xml
 * 2. Typography: Edit res/values/typography.xml
 * 3. Dimensions: Edit res/values/dimens.xml
 * 4. Themes: Edit res/values/themes.xml
 * 5. Fonts: Add font files to res/font/ and update typography.xml
 */
object DesignSystem {
    
    /**
     * Initialize design system
     * Call this in onCreate of MainActivity
     */
    fun initialize(context: Context) {
        // Apply any runtime theme configurations
        updateTheme(context)
    }
    
    /**
     * Update theme dynamically
     * Useful when theme needs to be changed at runtime
     */
    fun updateTheme(context: Context) {
        if (context is AppCompatActivity) {
            // Apply theme configurations
            // This can be extended to support dynamic theme switching
        }
    }
    
    /**
     * Check if dark theme is enabled
     */
    fun isDarkTheme(context: Context): Boolean {
        val nightModeFlags = context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
    
    /**
     * Get primary color
     */
    fun getPrimaryColor(context: Context): Int {
        return ContextCompat.getColor(context, com.example.lecom.R.color.primary)
    }
    
    /**
     * Get secondary color
     */
    fun getSecondaryColor(context: Context): Int {
        return ContextCompat.getColor(context, com.example.lecom.R.color.secondary)
    }
    
    /**
     * Get background color
     */
    fun getBackgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, com.example.lecom.R.color.background)
    }
    
    /**
     * Get text primary color
     */
    fun getTextPrimaryColor(context: Context): Int {
        return ContextCompat.getColor(context, com.example.lecom.R.color.text_primary)
    }
    
    /**
     * Get text secondary color
     */
    fun getTextSecondaryColor(context: Context): Int {
        return ContextCompat.getColor(context, com.example.lecom.R.color.text_secondary)
    }
    
    /**
     * Apply custom font family
     * To use custom fonts:
     * 1. Add font files (.ttf or .otf) to res/font/
     * 2. Update res/values/typography.xml with font family references
     * 3. Example: <string name="font_family_regular">@font/your_font</string>
     */
    fun getFontFamilyRegular(context: Context): String {
        return context.getString(com.example.lecom.R.string.font_family_regular)
    }
    
    fun getFontFamilyMedium(context: Context): String {
        return context.getString(com.example.lecom.R.string.font_family_medium)
    }
    
    fun getFontFamilyBold(context: Context): String {
        return context.getString(com.example.lecom.R.string.font_family_bold)
    }
}

