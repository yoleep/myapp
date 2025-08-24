import linearTheme from '../linear-theme.json';

export const theme = {
  colors: {
    brand: linearTheme.colors.brand,
    background: linearTheme.colors.background,
    text: linearTheme.colors.text,
    border: linearTheme.colors.border,
    link: linearTheme.colors.link,
    status: linearTheme.colors.status,
    functional: linearTheme.colors.functional,
  },
  typography: {
    fontFamily: linearTheme.typography.fontFamily,
    fontWeight: linearTheme.typography.fontWeight,
    fontSize: linearTheme.typography.fontSize,
    headings: linearTheme.typography.headings,
  },
  spacing: {
    radius: linearTheme.spacing.radius,
    layout: linearTheme.spacing.layout,
  },
  components: linearTheme.components,
  effects: linearTheme.effects,
  zIndex: linearTheme.zIndex,
  scrollbar: linearTheme.scrollbar,
};

export type Theme = typeof theme;