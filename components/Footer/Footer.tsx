import React from 'react';
import styled, { css } from 'styled-components';
import { Theme } from '../../styles/theme';

export interface FooterLink {
  label: string;
  href: string;
}

export interface FooterSection {
  title: string;
  links: FooterLink[];
}

export interface SocialLink {
  name: string;
  href: string;
  icon: React.ReactNode;
}

export interface FooterProps {
  logo?: React.ReactNode;
  title?: string;
  description?: string;
  sections?: FooterSection[];
  socialLinks?: SocialLink[];
  bottomText?: string;
  bottomLinks?: FooterLink[];
  variant?: 'default' | 'minimal' | 'centered';
}

const FooterContainer = styled.footer<{ variant: string; theme: Theme }>`
  background: ${props => props.theme.components.layout.footer.background};
  color: ${props => props.theme.components.layout.footer.color};
  border-top: 1px solid ${props => props.theme.colors.border.primary};
  margin-top: auto;
  
  ${props => props.variant === 'minimal' && css`
    padding: 24px 0;
  `}
  
  ${props => props.variant === 'default' && css`
    padding: 64px 0 32px;
  `}
  
  ${props => props.variant === 'centered' && css`
    padding: 48px 0;
    text-align: center;
  `}
`;

const FooterContent = styled.div<{ variant: string }>`
  max-width: ${props => props.theme.spacing.layout.pageMaxWidth};
  margin: 0 auto;
  padding: 0 ${props => props.theme.spacing.layout.pagePaddingInline};
  
  ${props => props.variant === 'centered' && css`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 32px;
  `}
`;

const FooterTop = styled.div<{ variant: string }>`
  display: grid;
  gap: 48px;
  margin-bottom: 48px;
  
  ${props => props.variant === 'default' && css`
    grid-template-columns: 2fr 1fr 1fr 1fr;
    
    @media (max-width: 768px) {
      grid-template-columns: 1fr;
      gap: 32px;
    }
  `}
  
  ${props => props.variant === 'centered' && css`
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    margin-bottom: 32px;
  `}
`;

const FooterBrand = styled.div`
  display: flex;
  flex-direction: column;
  gap: 16px;
`;

const FooterLogo = styled.div`
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: ${props => props.theme.typography.fontSize.large};
  font-weight: ${props => props.theme.typography.fontWeight.semibold};
  color: ${props => props.theme.colors.text.primary};
`;

const FooterDescription = styled.p`
  font-size: ${props => props.theme.typography.fontSize.regular};
  color: ${props => props.theme.colors.text.tertiary};
  line-height: 1.6;
  max-width: 400px;
`;

const FooterSectionContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 16px;
`;

const FooterSectionTitle = styled.h4`
  font-size: ${props => props.theme.typography.fontSize.small};
  font-weight: ${props => props.theme.typography.fontWeight.semibold};
  color: ${props => props.theme.colors.text.secondary};
  text-transform: uppercase;
  letter-spacing: 0.05em;
`;

const FooterLinks = styled.ul`
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
`;

const FooterLinkItem = styled.li``;

const StyledFooterLink = styled.a<{ theme: Theme }>`
  color: ${props => props.theme.colors.text.tertiary};
  text-decoration: none;
  font-size: ${props => props.theme.typography.fontSize.regular};
  transition: color ${props => props.theme.effects.transitions.quick} ease;
  
  &:hover {
    color: ${props => props.theme.colors.text.primary};
  }
`;

const SocialLinks = styled.div`
  display: flex;
  gap: 12px;
  margin-top: 24px;
`;

const SocialLink = styled.a<{ theme: Theme }>`
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: ${props => props.theme.colors.background.secondary};
  border: 1px solid ${props => props.theme.colors.border.secondary};
  border-radius: ${props => props.theme.spacing.radius.circle};
  color: ${props => props.theme.colors.text.tertiary};
  transition: all ${props => props.theme.effects.transitions.quick} ease;
  
  &:hover {
    background: ${props => props.theme.colors.background.tertiary};
    border-color: ${props => props.theme.colors.border.tertiary};
    color: ${props => props.theme.colors.text.primary};
    transform: translateY(-2px);
  }
`;

const FooterBottom = styled.div<{ theme: Theme }>`
  padding-top: 32px;
  border-top: 1px solid ${props => props.theme.colors.border.primary};
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
  
  @media (max-width: 768px) {
    flex-direction: column;
    text-align: center;
  }
`;

const FooterBottomText = styled.p`
  font-size: ${props => props.theme.typography.fontSize.small};
  color: ${props => props.theme.colors.text.quaternary};
`;

const FooterBottomLinks = styled.div`
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
  justify-content: center;
`;

const FooterBottomLink = styled.a<{ theme: Theme }>`
  font-size: ${props => props.theme.typography.fontSize.small};
  color: ${props => props.theme.colors.text.quaternary};
  text-decoration: none;
  transition: color ${props => props.theme.effects.transitions.quick} ease;
  
  &:hover {
    color: ${props => props.theme.colors.text.secondary};
  }
`;

const Divider = styled.span<{ theme: Theme }>`
  color: ${props => props.theme.colors.text.quaternary};
  margin: 0 8px;
`;

export const Footer: React.FC<FooterProps> = ({
  logo,
  title,
  description,
  sections = [],
  socialLinks = [],
  bottomText,
  bottomLinks = [],
  variant = 'default',
}) => {
  const showTopSection = logo || title || description || sections.length > 0 || socialLinks.length > 0;
  const showBottomSection = bottomText || bottomLinks.length > 0;

  return (
    <FooterContainer variant={variant}>
      <FooterContent variant={variant}>
        {showTopSection && (
          <FooterTop variant={variant}>
            {(logo || title || description || socialLinks.length > 0) && (
              <FooterBrand>
                {(logo || title) && (
                  <FooterLogo>
                    {logo}
                    {title && <span>{title}</span>}
                  </FooterLogo>
                )}
                {description && <FooterDescription>{description}</FooterDescription>}
                {socialLinks.length > 0 && (
                  <SocialLinks>
                    {socialLinks.map((social, index) => (
                      <SocialLink
                        key={index}
                        href={social.href}
                        aria-label={social.name}
                        title={social.name}
                      >
                        {social.icon}
                      </SocialLink>
                    ))}
                  </SocialLinks>
                )}
              </FooterBrand>
            )}
            
            {variant !== 'minimal' && sections.map((section, index) => (
              <FooterSectionContainer key={index}>
                <FooterSectionTitle>{section.title}</FooterSectionTitle>
                <FooterLinks>
                  {section.links.map((link, linkIndex) => (
                    <FooterLinkItem key={linkIndex}>
                      <StyledFooterLink href={link.href}>
                        {link.label}
                      </StyledFooterLink>
                    </FooterLinkItem>
                  ))}
                </FooterLinks>
              </FooterSectionContainer>
            ))}
          </FooterTop>
        )}
        
        {showBottomSection && (
          <FooterBottom>
            {bottomText && <FooterBottomText>{bottomText}</FooterBottomText>}
            {bottomLinks.length > 0 && (
              <FooterBottomLinks>
                {bottomLinks.map((link, index) => (
                  <React.Fragment key={index}>
                    {index > 0 && <Divider>·</Divider>}
                    <FooterBottomLink href={link.href}>
                      {link.label}
                    </FooterBottomLink>
                  </React.Fragment>
                ))}
              </FooterBottomLinks>
            )}
          </FooterBottom>
        )}
      </FooterContent>
    </FooterContainer>
  );
};