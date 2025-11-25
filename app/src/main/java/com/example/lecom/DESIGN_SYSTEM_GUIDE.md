# Design System Guide

This guide explains how to update the theme, fonts, and design of your app.

## Overview

The design system is structured to make it easy to update colors, fonts, typography, spacing, and themes from centralized locations.

## File Structure

```
res/
├── values/
│   ├── colors.xml          # Color definitions
│   ├── dimens.xml          # Spacing, sizes, and dimensions
│   ├── typography.xml      # Typography styles and font families
│   └── themes.xml          # Light theme configuration
├── values-night/
│   ├── colors.xml          # Dark theme colors
│   └── themes.xml          # Dark theme configuration
└── layout/
    └── activity_main.xml   # Main activity layout
```

## 1. Updating Colors

### Primary Colors
Edit `res/values/colors.xml`:

```xml
<color name="primary">#6200EE</color>           <!-- Main brand color -->
<color name="primary_dark">#3700B3</color>      <!-- Darker variant -->
<color name="primary_light">#BB86FC</color>      <!-- Lighter variant -->
<color name="on_primary">#FFFFFF</color>         <!-- Text on primary -->
```

### Secondary Colors
```xml
<color name="secondary">#03DAC5</color>
<color name="secondary_dark">#018786</color>
<color name="secondary_light">#03DAC5</color>
<color name="on_secondary">#000000</color>
```

### Background Colors
```xml
<color name="background">#FFFFFF</color>         <!-- Light theme background -->
<color name="surface">#FFFFFF</color>            <!-- Card/surface background -->
<color name="on_background">#000000</color>      <!-- Text on background -->
```

### Dark Theme Colors
Edit `res/values-night/colors.xml` for dark theme specific colors.

### Semantic Colors
- `error`, `error_light` - Error states
- `success`, `success_light` - Success states
- `warning`, `warning_light` - Warning states
- `info`, `info_light` - Info states

## 2. Updating Fonts

### Step 1: Add Font Files
1. Create `res/font/` directory if it doesn't exist
2. Add your font files (.ttf or .otf):
   - `font_regular.ttf`
   - `font_medium.ttf`
   - `font_bold.ttf`

### Step 2: Update Font Family References
Edit `res/values/typography.xml`:

```xml
<string name="font_family_regular">@font/your_font_regular</string>
<string name="font_family_medium">@font/your_font_medium</string>
<string name="font_family_bold">@font/your_font_bold</string>
```

### Step 3: Use System Fonts (Alternative)
If you want to use system fonts, keep the current values:
```xml
<string name="font_family_regular">sans-serif</string>
<string name="font_family_medium">sans-serif-medium</string>
<string name="font_family_bold">sans-serif-black</string>
```

## 3. Updating Typography

Edit `res/values/typography.xml` to customize text styles:

### Available Typography Styles
- `TextAppearance.Headline1` - 96sp
- `TextAppearance.Headline2` - 60sp
- `TextAppearance.Headline3` - 48sp
- `TextAppearance.Headline4` - 34sp
- `TextAppearance.Headline5` - 24sp
- `TextAppearance.Headline6` - 20sp (bold)
- `TextAppearance.Subtitle1` - 16sp
- `TextAppearance.Subtitle2` - 14sp (bold)
- `TextAppearance.Body1` - 16sp
- `TextAppearance.Body2` - 14sp
- `TextAppearance.Button` - 14sp (bold)
- `TextAppearance.Caption` - 12sp
- `TextAppearance.Overline` - 10sp (bold)

### Example: Customize Headline1
```xml
<style name="TextAppearance.Headline1">
    <item name="android:textSize">96sp</item>
    <item name="android:textStyle">normal</item>
    <item name="android:letterSpacing">-0.01562</item>
    <item name="fontFamily">@string/font_family_regular</item>
</style>
```

## 4. Updating Spacing and Dimensions

Edit `res/values/dimens.xml`:

### Spacing
```xml
<dimen name="spacing_xs">4dp</dimen>
<dimen name="spacing_small">8dp</dimen>
<dimen name="spacing_medium">16dp</dimen>
<dimen name="spacing_large">24dp</dimen>
<dimen name="spacing_xl">32dp</dimen>
```

### Corner Radius
```xml
<dimen name="radius_small">4dp</dimen>
<dimen name="radius_medium">8dp</dimen>
<dimen name="radius_large">12dp</dimen>
<dimen name="radius_xl">16dp</dimen>
```

### Button Heights
```xml
<dimen name="button_height">48dp</dimen>
<dimen name="button_height_small">36dp</dimen>
<dimen name="button_height_large">56dp</dimen>
```

## 5. Updating Themes

### Light Theme
Edit `res/values/themes.xml`:

```xml
<style name="Theme.Lecom" parent="Theme.MaterialComponents.DayNight.NoActionBar">
    <item name="colorPrimary">@color/primary</item>
    <item name="colorSecondary">@color/secondary</item>
    <item name="android:colorBackground">@color/background</item>
    <!-- Add more theme attributes -->
</style>
```

### Dark Theme
Edit `res/values-night/themes.xml` for dark theme specific configurations.

## 6. Using Design System in Code

### Access Colors Programmatically
```kotlin
import com.example.lecom.utils.DesignSystem

// Get colors
val primaryColor = DesignSystem.getPrimaryColor(context)
val backgroundColor = DesignSystem.getBackgroundColor(context)

// Check theme
val isDark = DesignSystem.isDarkTheme(context)
```

### Apply Typography in Layouts
```xml
<TextView
    android:textAppearance="@style/TextAppearance.Headline6"
    android:textColor="?attr/colorOnBackground" />
```

### Use Theme Attributes
```xml
<!-- Use theme colors that adapt to light/dark mode -->
<TextView
    android:textColor="?attr/colorOnBackground" />
    
<Button
    android:background="?attr/colorPrimary" />
```

## 7. Component Styles

### Buttons
- `Widget.Lecom.Button` - Primary button
- `Widget.Lecom.Button.Outlined` - Outlined button
- `Widget.Lecom.Button.Text` - Text button

### Cards
- `Widget.Lecom.Card` - Material card with custom styling

### Text Input
- `Widget.Lecom.TextInputLayout` - Text input with custom styling

## 8. Quick Customization Examples

### Change Primary Color to Blue
1. Edit `res/values/colors.xml`:
   ```xml
   <color name="primary">#2196F3</color>
   <color name="primary_dark">#1976D2</color>
   <color name="primary_light">#64B5F6</color>
   ```

### Change Font Size
1. Edit `res/values/typography.xml`:
   ```xml
   <style name="TextAppearance.Body1">
       <item name="android:textSize">18sp</item>  <!-- Changed from 16sp -->
   </style>
   ```

### Change Button Corner Radius
1. Edit `res/values/dimens.xml`:
   ```xml
   <dimen name="radius_medium">16dp</dimen>  <!-- Changed from 8dp -->
   ```

### Add Custom Font
1. Add font file to `res/font/custom_font.ttf`
2. Edit `res/values/typography.xml`:
   ```xml
   <string name="font_family_regular">@font/custom_font</string>
   ```

## 9. Best Practices

1. **Use Theme Attributes**: Always use `?attr/colorOnBackground` instead of hardcoded colors
2. **Consistent Spacing**: Use predefined spacing values from `dimens.xml`
3. **Typography Hierarchy**: Follow Material Design typography scale
4. **Dark Theme Support**: Always test both light and dark themes
5. **Accessibility**: Ensure sufficient color contrast ratios

## 10. Testing Your Changes

1. Run the app to see changes immediately
2. Test in both light and dark modes
3. Test on different screen sizes
4. Verify accessibility (color contrast, text sizes)

## Summary

- **Colors**: `res/values/colors.xml` and `res/values-night/colors.xml`
- **Fonts**: Add to `res/font/` and update `res/values/typography.xml`
- **Typography**: `res/values/typography.xml`
- **Spacing**: `res/values/dimens.xml`
- **Themes**: `res/values/themes.xml` and `res/values-night/themes.xml`

All design changes can be made by editing these resource files. The app will automatically pick up the changes on rebuild.

