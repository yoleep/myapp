import React, { ReactNode } from 'react';
import styled, { css } from 'styled-components';
import { Theme } from '../../styles/theme';
import { Navbar, NavbarProps } from '../Navbar';
import { Footer, FooterProps } from '../Footer';

export interface LayoutProps {
  children: ReactNode;
  navbar?: NavbarProps;
  footer?: FooterProps;
  sidebar?: {
    content: ReactNode;
    position?: 'left' | 'right';
    width?: string;
    collapsible?: boolean;
  };
  variant?: 'default' | 'fluid' | 'contained' | 'dashboard';
  padding?: boolean;
  backgroundColor?: string;
}

const LayoutContainer = styled.div<{ theme: Theme }>`
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: ${props => props.theme.colors.background.primary};
`;

const LayoutContent = styled.div<{ 
  hasSidebar?: boolean;
  sidebarPosition?: 'left' | 'right';
}>`
  flex: 1;
  display: flex;
  ${props => props.hasSidebar && css`
    flex-direction: ${props.sidebarPosition === 'left' ? 'row' : 'row-reverse'};
  `}
`;

const MainContent = styled.main<{ 
  variant: string;
  padding?: boolean;
  backgroundColor?: string;
  theme: Theme;
}>`
  flex: 1;
  width: 100%;
  
  ${props => props.variant === 'default' && css`
    max-width: ${props.theme.spacing.layout.pageMaxWidth};
    margin: 0 auto;
    padding: ${props.padding ? props.theme.spacing.layout.pagePaddingBlock : '0'} 
            ${props.padding ? props.theme.spacing.layout.pagePaddingInline : '0'};
  `}
  
  ${props => props.variant === 'fluid' && css`
    padding: ${props.padding ? '32px' : '0'};
  `}
  
  ${props => props.variant === 'contained' && css`
    max-width: ${props.theme.spacing.layout.proseMaxWidth};
    margin: 0 auto;
    padding: ${props.padding ? props.theme.spacing.layout.pagePaddingBlock : '0'} 
            ${props.padding ? props.theme.spacing.layout.pagePaddingInline : '0'};
  `}
  
  ${props => props.variant === 'dashboard' && css`
    padding: ${props.padding ? '24px' : '0'};
    height: calc(100vh - ${props.theme.components.header.height});
    overflow-y: auto;
  `}
  
  ${props => props.backgroundColor && css`
    background: ${props.backgroundColor};
  `}
`;

const Sidebar = styled.aside<{ 
  width?: string;
  position?: 'left' | 'right';
  theme: Theme;
}>`
  width: ${props => props.width || '260px'};
  background: ${props => props.theme.colors.background.secondary};
  border-${props => props.position === 'left' ? 'right' : 'left'}: 1px solid ${props => props.theme.colors.border.primary};
  overflow-y: auto;
  
  @media (max-width: 768px) {
    display: none;
  }
`;

const SidebarContent = styled.div`
  padding: 24px;
`;

export const Layout: React.FC<LayoutProps> = ({
  children,
  navbar,
  footer,
  sidebar,
  variant = 'default',
  padding = true,
  backgroundColor,
}) => {
  return (
    <LayoutContainer>
      {navbar && <Navbar {...navbar} />}
      
      <LayoutContent 
        hasSidebar={!!sidebar} 
        sidebarPosition={sidebar?.position}
      >
        {sidebar && (
          <Sidebar width={sidebar.width} position={sidebar.position}>
            <SidebarContent>{sidebar.content}</SidebarContent>
          </Sidebar>
        )}
        
        <MainContent 
          variant={variant} 
          padding={padding}
          backgroundColor={backgroundColor}
        >
          {children}
        </MainContent>
      </LayoutContent>
      
      {footer && <Footer {...footer} />}
    </LayoutContainer>
  );
};