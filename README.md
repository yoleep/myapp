# 🎨 Linear Design System Project

A modern web application built with Linear.app's design system, featuring a comprehensive component library with dark theme.

## 🚀 Quick Start

```bash
# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build

# Start production server
npm start
```

Visit `http://localhost:3000` to see the application.

## 📚 Documentation

### Component Demo
Visit `http://localhost:3000/components` to see all available components with interactive examples.

### Component Rules
**IMPORTANT**: Read [COMPONENT_RULES.md](./COMPONENT_RULES.md) before developing. This document contains mandatory rules for using the design system components.

## 🏗 Tech Stack

- **Framework**: Next.js 15.5
- **UI Library**: React 19
- **Styling**: Styled Components 6
- **Language**: TypeScript 5
- **Theme**: Linear.app inspired dark theme

## 📁 Project Structure

```
myapp/
├── components/           # Reusable UI components
│   ├── Button/
│   ├── Input/
│   ├── Card/
│   ├── List/
│   ├── Pagination/
│   ├── Layout/
│   ├── Navbar/
│   ├── Footer/
│   ├── Hero/
│   └── ...
├── pages/               # Next.js pages
│   ├── index.tsx       # Home page
│   └── components.tsx  # Component showcase
├── styles/             # Global styles and theme
│   ├── theme.ts       # Linear theme configuration
│   └── GlobalStyles.ts
├── linear-theme.json   # Complete Linear design tokens
└── COMPONENT_RULES.md  # Component usage guidelines
```

## 🎨 Available Components

### Core Components
- **Button**: Primary, Secondary, Ghost, Danger variants
- **Input**: Text input with label, error, and icon support
- **Select**: Custom dropdown with search
- **Card**: Content container with image support
- **Badge**: Status indicators and labels
- **Avatar**: User profile images with status

### Layout Components
- **Layout**: Page structure with navbar/footer/sidebar
- **Navbar**: Navigation header with mobile support
- **Footer**: Multi-section footer
- **Hero**: Landing page hero sections

### Data Display
- **List**: Table/Card view with sorting and selection
- **Pagination**: Page navigation with customization
- **Carousel**: Image/content slider
- **ThemeShowcase**: Visual theme documentation

## 🎯 Design Principles

1. **Dark Mode First**: Optimized for dark theme
2. **Component-Based**: Use provided components, no custom HTML
3. **Theme Consistency**: Use theme tokens, no hardcoded values
4. **Responsive**: Mobile-first design
5. **Accessible**: Keyboard navigation support

## 📋 Development Rules

### ✅ DO's
- Use `<Button>` instead of `<button>`
- Use `<Input>` instead of `<input>`
- Use `<Card>` for content grouping
- Use `<List>` instead of `<table>`
- Use theme colors from `theme.ts`
- Use `<Layout>` for all pages

### ❌ DON'Ts
- Don't create custom buttons
- Don't use inline styles with hardcoded colors
- Don't use native HTML form elements
- Don't create custom CSS classes
- Don't modify theme files

## 🔧 Scripts

```json
{
  "dev": "next dev",           // Start development server
  "build": "next build",       // Build for production
  "start": "next start",       // Start production server
  "lint": "next lint"          // Run ESLint
}
```

## 🎨 Theme Structure

The theme is based on Linear.app's design system:

```typescript
theme = {
  colors: {
    brand: { primary, accent, ... },
    background: { primary, secondary, ... },
    text: { primary, secondary, ... },
    border: { primary, secondary, ... },
    status: { red, green, orange, yellow }
  },
  typography: {
    fontFamily: { primary, monospace, ... },
    fontSize: { micro, mini, small, regular, large, ... },
    fontWeight: { light, normal, medium, semibold, bold }
  },
  spacing: {
    radius: { 4, 6, 8, 12, 16, 24, rounded, circle },
    layout: { headerHeight, pageMaxWidth, ... }
  },
  effects: {
    shadows: { none, low },
    transitions: { quick, regular },
    easing: { inQuad, outQuad, inOutQuad }
  }
}
```

## 📝 Example Usage

### Basic Page Layout
```tsx
import { Layout, Hero, Card, Button } from '@/components';

export default function HomePage() {
  return (
    <Layout
      navbar={{ title: 'MyApp', links: [...] }}
      footer={{ sections: [...] }}
    >
      <Hero 
        title="Welcome"
        subtitle="Build amazing apps"
        primaryAction={<Button variant="primary">Get Started</Button>}
      />
      <Card>
        <h2>Features</h2>
        <p>Content here...</p>
      </Card>
    </Layout>
  );
}
```

### Data Table with Pagination
```tsx
import { List, Pagination } from '@/components';

function DataTable({ data }) {
  const [page, setPage] = useState(1);
  
  return (
    <>
      <List 
        data={paginatedData}
        columns={columns}
        selectable
      />
      <Pagination
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
      />
    </>
  );
}
```

## 🚦 Component Status

All components are production-ready and tested:

| Component | Status | Mobile | TypeScript |
|-----------|--------|--------|------------|
| Button | ✅ Ready | ✅ Yes | ✅ Full |
| Input | ✅ Ready | ✅ Yes | ✅ Full |
| Card | ✅ Ready | ✅ Yes | ✅ Full |
| List | ✅ Ready | ✅ Yes | ✅ Full |
| Pagination | ✅ Ready | ✅ Yes | ✅ Full |
| Layout | ✅ Ready | ✅ Yes | ✅ Full |
| Navbar | ✅ Ready | ✅ Yes | ✅ Full |
| Footer | ✅ Ready | ✅ Yes | ✅ Full |
| Hero | ✅ Ready | ✅ Yes | ✅ Full |
| Select | ✅ Ready | ✅ Yes | ✅ Full |
| Badge | ✅ Ready | ✅ Yes | ✅ Full |
| Avatar | ✅ Ready | ✅ Yes | ✅ Full |
| Carousel | ✅ Ready | ✅ Yes | ✅ Full |

## 🤝 Contributing

1. Read [COMPONENT_RULES.md](./COMPONENT_RULES.md)
2. Use existing components
3. Follow theme system
4. Test on mobile
5. Ensure TypeScript types

## 📄 License

Private project - All rights reserved

## 🔗 Resources

- [Component Demo](http://localhost:3000/components)
- [Component Rules](./COMPONENT_RULES.md)
- [Linear.app](https://linear.app) - Design inspiration

---

Built with ❤️ using Linear Design System