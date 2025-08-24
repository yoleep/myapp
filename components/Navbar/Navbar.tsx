import React, { useState, useEffect } from 'react';
import styled, { css } from 'styled-components';
import { Theme } from '../../styles/theme';
import { Button } from '../Button';

export interface NavLink {
  label: string;
  href: string;
  badge?: string | number;
  isActive?: boolean;
}

export interface NavbarProps {
  logo?: React.ReactNode;
  title?: string;
  links?: NavLink[];
  rightContent?: React.ReactNode;
  sticky?: boolean;
  transparent?: boolean;
  onLogoClick?: () => void;
}

const NavbarContainer = styled.nav<{ 
  sticky?: boolean;
  transparent?: boolean;
  scrolled?: boolean;
  theme: Theme;
}>`
  width: 100%;
  height: ${props => props.theme.components.header.height};
  background: ${props => {
    if (props.transparent && !props.scrolled) {
      return 'transparent';
    }
    return props.theme.components.header.background;
  }};
  backdrop-filter: ${props => {
    if (props.transparent && !props.scrolled) {
      return 'none';
    }
    return `blur(${props.theme.components.header.backdrop})`;
  }};
  border-bottom: 1px solid ${props => {
    if (props.transparent && !props.scrolled) {
      return 'transparent';
    }
    return props.theme.components.header.border;
  }};
  transition: all ${props => props.theme.effects.transitions.regular} ease;
  z-index: ${props => props.theme.components.header.zIndex};
  
  ${props => props.sticky && css`
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
  `}
`;

const NavbarContent = styled.div`
  max-width: ${props => props.theme.spacing.layout.pageMaxWidth};
  height: 100%;
  margin: 0 auto;
  padding: 0 ${props => props.theme.spacing.layout.pagePaddingInline};
  display: flex;
  align-items: center;
  justify-content: space-between;
`;

const NavbarLeft = styled.div`
  display: flex;
  align-items: center;
  gap: 32px;
`;

const Logo = styled.div<{ clickable?: boolean }>`
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: ${props => props.theme.typography.fontSize.large};
  font-weight: ${props => props.theme.typography.fontWeight.semibold};
  color: ${props => props.theme.colors.text.primary};
  ${props => props.clickable && css`
    cursor: pointer;
  `}
`;

const NavLinks = styled.ul`
  display: flex;
  align-items: center;
  gap: 4px;
  list-style: none;
  margin: 0;
  padding: 0;
`;

const NavLinkItem = styled.li`
  position: relative;
`;

const StyledNavLink = styled.a<{ isActive?: boolean; theme: Theme }>`
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  border-radius: ${props => props.theme.spacing.radius['8']};
  color: ${props => props.isActive 
    ? props.theme.colors.text.primary 
    : props.theme.colors.text.tertiary};
  font-size: ${props => props.theme.typography.fontSize.regular};
  font-weight: ${props => props.theme.typography.fontWeight.medium};
  text-decoration: none;
  transition: all ${props => props.theme.effects.transitions.quick} ease;
  position: relative;
  
  &:hover {
    color: ${props => props.theme.colors.text.primary};
    background: ${props => props.theme.colors.background.translucent};
  }
  
  ${props => props.isActive && css`
    background: ${props.theme.colors.background.secondary};
    
    &::after {
      content: '';
      position: absolute;
      bottom: -1px;
      left: 12px;
      right: 12px;
      height: 2px;
      background: ${props.theme.colors.brand.primary};
      border-radius: 2px;
    }
  `}
`;

const NavBadge = styled.span<{ theme: Theme }>`
  padding: 2px 6px;
  background: ${props => props.theme.colors.brand.primary};
  color: ${props => props.theme.colors.brand.primaryText};
  font-size: ${props => props.theme.typography.fontSize.micro};
  font-weight: ${props => props.theme.typography.fontWeight.semibold};
  border-radius: ${props => props.theme.spacing.radius.rounded};
  line-height: 1;
`;

const NavbarRight = styled.div`
  display: flex;
  align-items: center;
  gap: 12px;
`;

const MobileMenuButton = styled.button<{ theme: Theme }>`
  display: none;
  width: 32px;
  height: 32px;
  padding: 0;
  background: transparent;
  border: none;
  color: ${props => props.theme.colors.text.primary};
  cursor: pointer;
  align-items: center;
  justify-content: center;
  
  @media (max-width: 768px) {
    display: flex;
  }
`;

const MobileMenu = styled.div<{ isOpen: boolean; theme: Theme }>`
  display: none;
  position: fixed;
  top: ${props => props.theme.components.header.height};
  left: 0;
  right: 0;
  background: ${props => props.theme.colors.background.primary};
  border-bottom: 1px solid ${props => props.theme.colors.border.primary};
  padding: 16px;
  transform: ${props => props.isOpen ? 'translateY(0)' : 'translateY(-100%)'};
  opacity: ${props => props.isOpen ? 1 : 0};
  pointer-events: ${props => props.isOpen ? 'auto' : 'none'};
  transition: all ${props => props.theme.effects.transitions.regular} ease;
  z-index: ${props => parseInt(props.theme.components.header.zIndex) - 1};
  
  @media (max-width: 768px) {
    display: block;
  }
`;

const MobileNavLinks = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

const DesktopOnly = styled.div`
  display: flex;
  align-items: center;
  gap: 12px;
  
  @media (max-width: 768px) {
    display: none;
  }
`;

export const Navbar: React.FC<NavbarProps> = ({
  logo,
  title,
  links = [],
  rightContent,
  sticky = false,
  transparent = false,
  onLogoClick,
}) => {
  const [scrolled, setScrolled] = useState(false);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  useEffect(() => {
    if (sticky || transparent) {
      const handleScroll = () => {
        setScrolled(window.scrollY > 10);
      };
      
      window.addEventListener('scroll', handleScroll);
      return () => window.removeEventListener('scroll', handleScroll);
    }
  }, [sticky, transparent]);

  return (
    <>
      <NavbarContainer sticky={sticky} transparent={transparent} scrolled={scrolled}>
        <NavbarContent>
          <NavbarLeft>
            <Logo clickable={!!onLogoClick} onClick={onLogoClick}>
              {logo}
              {title && <span>{title}</span>}
            </Logo>
            
            <DesktopOnly>
              <NavLinks>
                {links.map((link, index) => (
                  <NavLinkItem key={index}>
                    <StyledNavLink href={link.href} isActive={link.isActive}>
                      {link.label}
                      {link.badge && <NavBadge>{link.badge}</NavBadge>}
                    </StyledNavLink>
                  </NavLinkItem>
                ))}
              </NavLinks>
            </DesktopOnly>
          </NavbarLeft>
          
          <NavbarRight>
            <DesktopOnly>
              {rightContent}
            </DesktopOnly>
            
            <MobileMenuButton onClick={() => setMobileMenuOpen(!mobileMenuOpen)}>
              {mobileMenuOpen ? (
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <path d="M18 6L6 18M6 6l12 12" />
                </svg>
              ) : (
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <path d="M3 12h18M3 6h18M3 18h18" />
                </svg>
              )}
            </MobileMenuButton>
          </NavbarRight>
        </NavbarContent>
      </NavbarContainer>
      
      <MobileMenu isOpen={mobileMenuOpen}>
        <MobileNavLinks>
          {links.map((link, index) => (
            <StyledNavLink key={index} href={link.href} isActive={link.isActive}>
              {link.label}
              {link.badge && <NavBadge>{link.badge}</NavBadge>}
            </StyledNavLink>
          ))}
        </MobileNavLinks>
        {rightContent && (
          <div style={{ marginTop: '16px' }}>
            {rightContent}
          </div>
        )}
      </MobileMenu>
      
      {sticky && <div style={{ height: '64px' }} />}
    </>
  );
};