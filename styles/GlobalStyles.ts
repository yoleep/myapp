import { createGlobalStyle } from 'styled-components';
import { Theme } from './theme';

export const GlobalStyles = createGlobalStyle<{ theme: Theme }>`
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }

  html {
    font-size: 16px;
  }

  body {
    background: ${props => props.theme.colors.background.primary};
    color: ${props => props.theme.colors.text.primary};
    font-family: ${props => props.theme.typography.fontFamily.primary};
    font-weight: ${props => props.theme.typography.fontWeight.normal};
    line-height: 1.5;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }

  h1, h2, h3, h4, h5, h6 {
    font-weight: ${props => props.theme.typography.fontWeight.semibold};
    line-height: 1.2;
  }

  h1 {
    font-size: ${props => props.theme.typography.headings.h1.fontSize};
    font-weight: ${props => props.theme.typography.headings.h1.fontWeight};
    line-height: ${props => props.theme.typography.headings.h1.lineHeight};
    letter-spacing: ${props => props.theme.typography.headings.h1.letterSpacing};
    color: ${props => props.theme.typography.headings.h1.color};
  }

  h2 {
    font-size: ${props => props.theme.typography.headings.h2.fontSize};
    font-weight: ${props => props.theme.typography.headings.h2.fontWeight};
    line-height: ${props => props.theme.typography.headings.h2.lineHeight};
    letter-spacing: ${props => props.theme.typography.headings.h2.letterSpacing};
    color: ${props => props.theme.typography.headings.h2.color};
  }

  h3 {
    font-size: ${props => props.theme.typography.headings.h3.fontSize};
    font-weight: ${props => props.theme.typography.headings.h3.fontWeight};
    line-height: ${props => props.theme.typography.headings.h3.lineHeight};
    letter-spacing: ${props => props.theme.typography.headings.h3.letterSpacing};
    color: ${props => props.theme.typography.headings.h3.color};
  }

  a {
    color: ${props => props.theme.colors.link.primary};
    text-decoration: none;
    transition: color ${props => props.theme.effects.transitions.quick} ease;

    &:hover {
      color: ${props => props.theme.colors.link.hover};
    }
  }

  button {
    font-family: inherit;
    cursor: pointer;
  }

  input, textarea, select {
    font-family: inherit;
  }

  code, pre {
    font-family: ${props => props.theme.typography.fontFamily.monospace};
  }

  ::-webkit-scrollbar {
    width: ${props => props.theme.scrollbar.size};
    height: ${props => props.theme.scrollbar.size};
  }

  ::-webkit-scrollbar-track {
    background: transparent;
  }

  ::-webkit-scrollbar-thumb {
    background: ${props => props.theme.scrollbar.color};
    border-radius: ${props => props.theme.spacing.radius.rounded};
    
    &:hover {
      background: ${props => props.theme.scrollbar.colorHover};
    }
    
    &:active {
      background: ${props => props.theme.scrollbar.colorActive};
    }
  }
`;