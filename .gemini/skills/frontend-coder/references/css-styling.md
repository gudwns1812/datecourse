# Tailwind CSS Styling Reference

Comprehensive reference for Tailwind CSS, utility-first styling, and responsive design patterns.

## Core Concepts

### Tailwind CSS

Utility-first CSS framework for building custom designs without leaving your HTML. Apply styles directly using class names.

**Three Approaches to Tailwind**:

1. **Utility Classes**: Direct styling with class names - `<div class="flex gap-4 p-6">`
2. **Component Classes**: Extract reusable components with `@apply`
3. **Plugins**: Extend Tailwind with custom utilities

### Class-based Styling

Tailwind uses semantic class names that describe what they do:

**Common Patterns**:

1. **Sizing**: `w-full`, `h-screen`, `max-w-md`
2. **Spacing**: `p-4`, `m-8`, `gap-2`
3. **Colors**: `bg-blue-500`, `text-gray-700`
4. **Layout**: `flex`, `grid`, `block`
5. **States**: `hover:bg-blue-600`, `focus:ring-2`, `dark:bg-slate-900`

### Responsive Design with Breakpoints

Tailwind's responsive prefixes apply styles at different screen sizes:

| Prefix | Breakpoint | Example |
|--------|-----------|---------|
| (none) | 0px | `flex` |
| `sm:` | 640px | `sm:flex-row` |
| `md:` | 768px | `md:w-1/2` |
| `lg:` | 1024px | `lg:grid-cols-3` |
| `xl:` | 1280px | `xl:max-w-6xl` |
| `2xl:` | 1536px | `2xl:text-2xl` |

## Core Utilities

### Sizing

```tsx
// Width utilities
<div className="w-full">Full width</div>
<div className="w-1/2">50% width</div>
<div className="w-64">Fixed width (256px)</div>
<div className="w-auto">Auto width</div>
<div className="max-w-lg">Max width</div>
<div className="min-w-0">Min width</div>

// Height utilities
<div className="h-screen">Full viewport height</div>
<div className="h-64">Fixed height (256px)</div>
<div className="h-auto">Auto height</div>
<div className="min-h-screen">Min height</div>
```

### Spacing (Padding & Margin)

```tsx
// Padding - uniform
<div className="p-4">Padding all sides (1rem)</div>
<div className="px-6">Padding horizontal (1.5rem)</div>
<div className="py-2">Padding vertical (0.5rem)</div>
<div className="pt-4">Padding top</div>
<div className="pr-6">Padding right</div>
<div className="pb-8">Padding bottom</div>
<div className="pl-2">Padding left</div>

// Margin
<div className="m-4">Margin all sides</div>
<div className="mx-auto">Margin horizontal (center)</div>
<div className="my-8">Margin vertical</div>
<div className="-m-2">Negative margin</div>

// Gap (between flex/grid items)
<div className="flex gap-4">
  <div>Item 1</div>
  <div>Item 2</div>
</div>
```

### Colors

```tsx
// Text colors
<p className="text-red-500">Red text</p>
<p className="text-gray-900 dark:text-white">Dark mode</p>

// Background colors
<div className="bg-blue-100">Light blue background</div>
<div className="bg-gradient-to-r from-blue-500 to-purple-600">Gradient</div>

// Border colors
<div className="border-2 border-green-500">Green border</div>

// State-based colors
<button className="bg-blue-500 hover:bg-blue-600 active:bg-blue-700 focus:ring-2 focus:ring-blue-300">
  Button
</button>
```

### Display & Visibility

```tsx
// Display
<div className="block">Block element</div>
<div className="inline">Inline element</div>
<div className="inline-block">Inline-block</div>
<div className="hidden">Hidden</div>
<div className="visible">Visible</div>

// Responsive visibility
<div className="hidden md:block">Hidden on mobile, visible on tablet+</div>
<div className="md:hidden">Visible on mobile, hidden on tablet+</div>
```

## Layout Systems

### Flexbox

One-dimensional layout system (row or column):

```tsx
// Container
<div className="flex flex-col gap-4">
  {/* Items are stacked vertically with gap */}
</div>

<div className="flex flex-row justify-between items-center gap-6">
  {/* Items are in a row, distributed with space between */}
</div>

// Direction
<div className="flex flex-row">Row layout</div>
<div className="flex flex-col">Column layout</div>
<div className="flex flex-row-reverse">Reverse row</div>
<div className="flex flex-col-reverse">Reverse column</div>

// Wrapping
<div className="flex flex-wrap">Wrap items to next line</div>
<div className="flex flex-wrap-reverse">Reverse wrap direction</div>

// Justify content (main axis alignment)
<div className="flex justify-start">Align left</div>
<div className="flex justify-center">Center horizontally</div>
<div className="flex justify-end">Align right</div>
<div className="flex justify-between">Space between items</div>
<div className="flex justify-around">Space around items</div>
<div className="flex justify-evenly">Equal space between</div>

// Align items (cross axis alignment)
<div className="flex items-start">Align top</div>
<div className="flex items-center">Center vertically</div>
<div className="flex items-end">Align bottom</div>
<div className="flex items-stretch">Stretch to fill</div>

// Item growth
<div className="flex">
  <div className="flex-1">Grows equally (flex-grow: 1)</div>
  <div className="flex-1">Grows equally</div>
  <div className="flex-none">No growth (flex: none)</div>
</div>

// Align individual item
<div className="flex items-center">
  <div>Item 1</div>
  <div className="self-start">Align to start</div>
  <div className="self-end">Align to end</div>
</div>
```

### CSS Grid

Two-dimensional layout system (rows and columns):

```tsx
// Container
<div className="grid grid-cols-3 gap-4">
  {/* Three equal columns */}
</div>

<div className="grid grid-cols-12 gap-6">
  {/* 12 column grid */}
</div>

// Responsive columns
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
  {/* 1 column on mobile, 2 on tablet, 4 on desktop */}
</div>

// Auto-fit (responsive)
<div className="grid auto-cols-max gap-4">
  {/* Items size themselves */}
</div>

// Item spanning
<div className="grid grid-cols-4 gap-4">
  <div className="col-span-2">Spans 2 columns</div>
  <div className="col-span-1">Spans 1 column</div>
  <div className="row-span-2">Spans 2 rows</div>
</div>

// Alignment
<div className="grid grid-cols-3 gap-4 justify-items-center items-center">
  {/* All items centered */}
</div>

<div className="grid grid-cols-3 gap-4">
  <div className="justify-self-start">Align left</div>
  <div className="self-end">Align bottom</div>
</div>
```

### Flex vs Grid

| Use Case | Best Choice |
|----------|-------------|
| One-dimensional layout (row or column) | Flexbox - `flex` |
| Two-dimensional layout (rows and columns) | Grid - `grid` |
| Align items along one axis | Flexbox - `flex` |
| Create complex page layouts | Grid - `grid` |
| Navigation menus | Flexbox - `flex` |
| Precise control over rows and columns | Grid - `grid` |
| Space between elements | Flexbox - `flex gap-` |
| Card/dashboard layouts | Grid - `grid grid-cols-` |

## Positioning

### Position Types

```tsx
// Static (default)
<div className="static">Normal flow</div>

// Relative - offset from normal position
<div className="relative top-4 left-8">Offset position</div>

// Absolute - positioned relative to nearest positioned ancestor
<div className="relative">
  <div className="absolute top-0 right-0">Top right corner</div>
</div>

// Fixed - positioned relative to viewport
<div className="fixed bottom-4 right-4">
  Floating button
</div>

// Sticky - switches between relative and fixed on scroll
<div className="sticky top-0">
  Sticks to top when scrolling
</div>

// Inset (positioning shorthand)
<div className="absolute inset-0">
  Cover entire container
</div>

<div className="absolute inset-x-0 top-0">
  Full width, stick to top
</div>
```

### Layering (z-index)

```tsx
<div className="relative z-10">Higher layer</div>
<div className="relative z-20">Even higher</div>
<div className="relative z-0">Back layer</div>
<div className="relative -z-10">Behind all</div>
```

**Note**: Use `relative`, `absolute`, `fixed`, or `sticky` to enable `z-index`

## Responsive Design

### Mobile-First Approach

```tsx
// Base mobile styles, enhance upward
<div className="
  grid grid-cols-1
  md:grid-cols-2
  lg:grid-cols-3
  xl:grid-cols-4
  gap-4 p-4
  md:p-8
">
  {/* 1 column on mobile, 2 on tablets, 3 on laptops, 4 on desktops */}
</div>

// Container queries (modern)
<div className="@container">
  <div className="@md:grid @md:grid-cols-2">
    Responsive to container size
  </div>
</div>
```

### Dark Mode

```tsx
// Automatic dark mode (respects system preference)
<div className="bg-white dark:bg-slate-900 text-black dark:text-white">
  Works in both light and dark
</div>

// Class-based dark mode
<div className="dark bg-white dark:bg-slate-900">
  Forced dark mode
</div>

// Component with dark mode
export function Card() {
  return (
    <div className="rounded-lg bg-white p-6 shadow dark:bg-slate-800 dark:shadow-xl">
      <h2 className="text-gray-900 dark:text-white">
        Card Title
      </h2>
      <p className="text-gray-600 dark:text-gray-300">
        Card content
      </p>
    </div>
  );
}
```

## Typography

```tsx
// Font size
<h1 className="text-4xl">Large heading</h1>
<h2 className="text-3xl">Medium heading</h2>
<p className="text-base">Body text</p>
<small className="text-sm">Small text</small>
<span className="text-xs">Extra small</span>

// Font weight
<p className="font-thin">Thin (100)</p>
<p className="font-light">Light (300)</p>
<p className="font-normal">Normal (400)</p>
<p className="font-medium">Medium (500)</p>
<p className="font-semibold">Semibold (600)</p>
<p className="font-bold">Bold (700)</p>
<p className="font-black">Black (900)</p>

// Font style
<p className="italic">Italic text</p>
<p className="not-italic">Not italic</p>

// Line height
<p className="leading-tight">Tight line height</p>
<p className="leading-normal">Normal line height</p>
<p className="leading-relaxed">Relaxed line height</p>
<p className="leading-loose">Loose line height</p>

// Letter spacing
<p className="tracking-tight">Tight tracking</p>
<p className="tracking-normal">Normal tracking</p>
<p className="tracking-wide">Wide tracking</p>

// Text alignment
<p className="text-left">Left aligned</p>
<p className="text-center">Centered</p>
<p className="text-right">Right aligned</p>
<p className="text-justify">Justified</p>

// Text decoration
<a className="underline">Underlined link</a>
<a className="no-underline">No underline</a>
<p className="line-through">Strikethrough</p>
<p className="overline">Overline</p>

// Text transform
<p className="uppercase">UPPERCASE</p>
<p className="lowercase">lowercase</p>
<p className="capitalize">Capitalize</p>
<p className="normal-case">normal case</p>

// Text overflow
<p className="truncate">
  This text will be truncated with ellipsis...
</p>
<p className="overflow-hidden text-ellipsis whitespace-nowrap">
  Single line with ellipsis
</p>

// Whitespace
<pre className="whitespace-pre">Preserve whitespace</pre>
<p className="whitespace-nowrap">No wrap text</p>
<p className="whitespace-normal">Normal wrap</p>
```

## Colors & Gradients

```tsx
// Text colors - using color palette (50-950)
<p className="text-red-500">Red text</p>
<p className="text-blue-900">Dark blue text</p>
<p className="text-gray-50">Very light gray</p>

// Background colors
<div className="bg-green-100">Light green background</div>
<div className="bg-purple-500">Purple background</div>

// Border colors
<div className="border-2 border-orange-400">Orange border</div>

// State-based colors
<button className="
  bg-blue-500
  hover:bg-blue-600
  active:bg-blue-700
  disabled:bg-gray-400
  focus:ring-2
  focus:ring-blue-300
">
  Interactive button
</button>

// Gradients
<div className="bg-gradient-to-r from-blue-500 to-purple-600">
  Left to right gradient
</div>

<div className="bg-gradient-to-b from-green-400 via-blue-500 to-purple-600">
  Top to bottom with middle color
</div>

// Opacity
<div className="bg-red-500 opacity-50">Semi-transparent</div>
<div className="text-blue-600 opacity-75">75% opacity text</div>
```

## Borders & Outlines

```tsx
// Border width
<div className="border">1px border</div>
<div className="border-2">2px border</div>
<div className="border-4">4px border</div>
<div className="border-8">8px border</div>

// Border color
<div className="border-2 border-blue-500">Blue border</div>
<div className="border-2 border-gray-300 hover:border-gray-400">
  State-based border
</div>

// Border radius
<div className="rounded">Rounded corners</div>
<div className="rounded-lg">Large radius</div>
<div className="rounded-full">Completely rounded</div>
<div className="rounded-t-lg">Top radius only</div>
<div className="rounded-br-xl">Bottom-right radius</div>

// Outline (for focus states)
<button className="outline-none focus:outline-2 focus:outline-blue-500">
  Button with focus outline
</button>

// Ring (box-shadow for focus)
<input className="focus:ring-2 focus:ring-blue-500 focus:ring-offset-2" />
```

## Shadows & Effects

```tsx
// Box shadows
<div className="shadow">Small shadow</div>
<div className="shadow-md">Medium shadow</div>
<div className="shadow-lg">Large shadow</div>
<div className="shadow-xl">Extra large shadow</div>
<div className="shadow-2xl">2xl shadow</div>
<div className="shadow-inner">Inner shadow</div>

// Color shadows
<div className="shadow-lg shadow-red-500/50">Red-tinted shadow</div>

// Opacity
<div className="shadow-lg shadow-black/50">Semi-transparent shadow</div>

// Drop shadow (filter)
<img className="drop-shadow-lg" src="..." />

// Backdrop blur (glassmorphism)
<div className="backdrop-blur-md bg-white/80">
  Frosted glass effect
</div>
```

## Transformations & Transitions

### Transforms

```tsx
// Translate (move)
<div className="translate-x-4">Move right 16px</div>
<div className="translate-y-8">Move down 32px</div>
<div className="translate-x-1/2 -translate-y-1/2">Center position</div>

// Scale
<div className="scale-75">75% size</div>
<div className="scale-125">125% size</div>
<div className="scale-x-150">Scale X only</div>

// Rotate
<div className="rotate-45">45 degree rotation</div>
<div className="rotate-180">180 degrees</div>

// Skew
<div className="skew-x-12">Skew X axis</div>
<div className="skew-y-6">Skew Y axis</div>

// Origin (transform origin)
<div className="origin-center scale-150">Transform from center</div>
<div className="origin-top-right rotate-45">Rotate from top-right</div>

// Perspective
<div className="perspective">
  <div className="transform-gpu translate-z-0">3D effect</div>
</div>

// Interactive example
<div className="hover:scale-110 hover:shadow-xl transition-all duration-300">
  Hover to scale
</div>
```

### Transitions & Animations

```tsx
// Transition (smooth change)
<button className="transition-colors duration-300 hover:bg-blue-600">
  Hover for smooth color change
</button>

<div className="transition-all duration-300 ease-in-out hover:scale-110">
  Smooth all properties
</div>

// Delay
<div className="transition-opacity duration-500 delay-100">
  Delayed transition
</div>

// Timing functions
<div className="transition-all duration-300 ease-linear">Linear</div>
<div className="transition-all duration-300 ease-in">Ease in</div>
<div className="transition-all duration-300 ease-out">Ease out</div>
<div className="transition-all duration-300 ease-in-out">Ease in-out</div>

// Built-in animations
<div className="animate-spin">Spinning loader</div>
<div className="animate-pulse">Pulsing animation</div>
<div className="animate-bounce">Bouncing animation</div>
<a className="animate-wiggle">Custom animation</a>

// Complex example
export function Card() {
  return (
    <div className="
      bg-white
      rounded-lg
      shadow-md
      p-6
      transition-all
      duration-300
      hover:shadow-xl
      hover:scale-105
      hover:-translate-y-1
      cursor-pointer
    ">
      Interactive card
    </div>
  );
}
```

## Pseudo-Classes with Tailwind

```tsx
// Hover, focus, active
<button className="
  bg-blue-500
  hover:bg-blue-600
  focus:outline-none
  focus:ring-2
  focus:ring-blue-300
  active:scale-95
">
  Interactive button
</button>

// Form states
<input className="
  border
  border-gray-300
  focus:border-blue-500
  focus:ring-2
  focus:ring-blue-200
  disabled:bg-gray-100
  disabled:cursor-not-allowed
" />

// Group states (parent-based)
<div className="group hover:bg-gray-100">
  <p className="group-hover:text-blue-600">Text changes on parent hover</p>
</div>

// Sibling states
<input type="checkbox" defaultChecked />
<label className="peer-checked:text-green-600">
  Label changes when input is checked
</label>

// Dark mode
<div className="
  bg-white
  text-gray-900
  dark:bg-slate-900
  dark:text-white
  dark:hover:bg-slate-800
">
  Works in dark mode
</div>

// First, last, odd, even
<ul>
  <li className="first:pt-0 last:pb-0 odd:bg-gray-50">Item</li>
</ul>
```

## Accessible Components

```tsx
// Accessible button
export function Button() {
  return (
    <button className="
      inline-flex
      items-center
      justify-center
      px-4 py-2
      rounded-lg
      bg-blue-600
      text-white
      font-medium
      hover:bg-blue-700
      focus:outline-none
      focus:ring-2
      focus:ring-blue-500
      focus:ring-offset-2
      disabled:opacity-50
      disabled:cursor-not-allowed
      transition-colors
    ">
      Click me
    </button>
  );
}

// Accessible form
export function Form() {
  return (
    <form className="space-y-4">
      <div>
        <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
          Email:
        </label>
        <input
          id="email"
          type="email"
          required
          className="
            w-full
            px-3 py-2
            border border-gray-300
            rounded-lg
            focus:outline-none
            focus:ring-2
            focus:ring-blue-500
            focus:border-transparent
          "
        />
      </div>
      <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700">
        Submit
      </button>
    </form>
  );
}
```

## Best Practices

### Do's

- ✅ Use utility classes directly in your HTML/JSX
- ✅ Leverage responsive prefixes (`md:`, `lg:`) for mobile-first design
- ✅ Use Tailwind's built-in color palette
- ✅ Extract components when patterns repeat (use component files, not @apply)
- ✅ Use dark mode variants for theme support
- ✅ Combine utilities for complex effects
- ✅ Use `gap` for spacing between flex/grid items
- ✅ Take advantage of state variants (hover, focus, active)
- ✅ Use `sr-only` for screen reader-only content

### Don'ts

- ❌ Write custom CSS unless necessary
- ❌ Override Tailwind with `!important`
- ❌ Use meaningless classes like `flex-container`
- ❌ Forget responsive breakpoints
- ❌ Ignore accessibility requirements
- ❌ Over-customize the config (use defaults)
- ❌ Mix CSS and Tailwind inconsistently
- ❌ Use hardcoded values instead of Tailwind's scale
- ❌ Ignore dark mode support

## Glossary Terms

**Key Terms Covered**:

- Utility classes
- Responsive prefixes
- State variants
- Dark mode variants
- Custom utilities
- Plugin
- Class composition
- Component extraction
- Tailwind config
- Arbitrary values
- Group and peer selectors
- Pseudo-classes
- Pseudo-elements
- Breakpoints
- Color scale
- Spacing scale
- Z-index scale
- Border radius scale
- Shadow variants
- Animation utilities
- Transform utilities
- Transition utilities
- Ring utilities
- Backdrop utilities

## Additional Resources

- [Tailwind CSS Documentation](https://tailwindcss.com/docs)
- [Tailwind Component Library (Headless UI)](https://headlessui.dev/)
- [Tailwind UI (Paid Components)](https://tailwindui.com/)
- [Tailwind CSS SearchBox](https://tailwindcss.com/) - Search all utilities
- [TailwindCSS IntelliSense (VS Code)](https://marketplace.visualstudio.com/items?itemName=bradlc.vscode-tailwindcss)
