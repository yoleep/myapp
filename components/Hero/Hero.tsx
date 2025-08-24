import React from 'react';
import styled, { css, keyframes } from 'styled-components';
import { Theme } from '../../styles/theme';

export interface HeroProps {
  title: string;
  subtitle?: string;
  description?: string;
  primaryAction?: React.ReactNode;
  secondaryAction?: React.ReactNode;
  image?: {
    src: string;
    alt: string;
    position?: 'left' | 'right' | 'background';
  };
  variant?: 'default' | 'centered' | 'gradient' | 'minimal';
  height?: 'small' | 'medium' | 'large' | 'full';
  backgroundEffect?: 'none' | 'dots' | 'grid' | 'gradient';
}

const fadeIn = keyframes`
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
`;

const float = keyframes`
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
`;

const heights = {
  small: '400px',
  medium: '600px',
  large: '800px',
  full: '100vh',
};

const HeroContainer = styled.section<{ 
  height: string;
  hasBackgroundImage?: boolean;
  variant: string;
  theme: Theme;
}>`
  position: relative;
  width: 100%;
  min-height: ${props => heights[props.height as keyof typeof heights]};
  display: flex;
  align-items: center;
  overflow: hidden;
  background: ${props => props.theme.colors.background.primary};
  
  ${props => props.variant === 'gradient' && css`
    background: linear-gradient(135deg, 
      ${props.theme.colors.brand.primary}20 0%, 
      ${props.theme.colors.background.primary} 50%,
      ${props.theme.colors.brand.accent}10 100%);
  `}
  
  ${props => props.hasBackgroundImage && css`
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(
        to bottom,
        ${props.theme.colors.background.primary}00 0%,
        ${props.theme.colors.background.primary}99 50%,
        ${props.theme.colors.background.primary} 100%
      );
      z-index: 1;
    }
  `}
`;

const BackgroundEffect = styled.div<{ effect: string; theme: Theme }>`
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  
  ${props => props.effect === 'dots' && css`
    background-image: radial-gradient(
      circle at 1px 1px,
      ${props.theme.colors.border.primary} 1px,
      transparent 1px
    );
    background-size: 30px 30px;
    opacity: 0.5;
  `}
  
  ${props => props.effect === 'grid' && css`
    background-image: 
      linear-gradient(${props.theme.colors.border.primary}20 1px, transparent 1px),
      linear-gradient(90deg, ${props.theme.colors.border.primary}20 1px, transparent 1px);
    background-size: 50px 50px;
  `}
  
  ${props => props.effect === 'gradient' && css`
    background: radial-gradient(
      circle at 30% 50%,
      ${props.theme.colors.brand.primary}20 0%,
      transparent 50%
    );
  `}
`;

const HeroContent = styled.div<{ variant: string }>`
  position: relative;
  z-index: 2;
  max-width: ${props => props.theme.spacing.layout.pageMaxWidth};
  width: 100%;
  margin: 0 auto;
  padding: 0 ${props => props.theme.spacing.layout.pagePaddingInline};
  
  ${props => props.variant === 'centered' && css`
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
  `}
`;

const HeroGrid = styled.div<{ imagePosition?: string }>`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 64px;
  align-items: center;
  
  ${props => props.imagePosition === 'left' && css`
    direction: rtl;
    
    & > * {
      direction: ltr;
    }
  `}
  
  @media (max-width: 768px) {
    grid-template-columns: 1fr;
    gap: 32px;
  }
`;

const HeroTextContent = styled.div<{ variant: string }>`
  animation: ${fadeIn} 0.8s ease-out;
  
  ${props => props.variant === 'centered' && css`
    max-width: 800px;
  `}
`;

const HeroSubtitle = styled.span<{ theme: Theme }>`
  display: inline-block;
  font-size: ${props => props.theme.typography.fontSize.regular};
  font-weight: ${props => props.theme.typography.fontWeight.semibold};
  color: ${props => props.theme.colors.brand.primary};
  text-transform: uppercase;
  letter-spacing: 0.1em;
  margin-bottom: 16px;
  animation: ${fadeIn} 0.6s ease-out;
`;

const HeroTitle = styled.h1<{ variant: string; theme: Theme }>`
  font-size: ${props => props.theme.typography.headings.h1.fontSize};
  font-weight: ${props => props.theme.typography.headings.h1.fontWeight};
  line-height: ${props => props.theme.typography.headings.h1.lineHeight};
  letter-spacing: ${props => props.theme.typography.headings.h1.letterSpacing};
  color: ${props => props.theme.typography.headings.h1.color};
  margin-bottom: 24px;
  animation: ${fadeIn} 0.7s ease-out;
  
  ${props => props.variant === 'minimal' && css`
    font-size: 48px;
    line-height: 1.2;
  `}
  
  @media (max-width: 768px) {
    font-size: 40px;
    line-height: 1.2;
  }
`;

const HeroDescription = styled.p<{ theme: Theme }>`
  font-size: ${props => props.theme.typography.fontSize.large};
  line-height: 1.6;
  color: ${props => props.theme.colors.text.secondary};
  margin-bottom: 32px;
  max-width: 600px;
  animation: ${fadeIn} 0.8s ease-out;
`;

const HeroActions = styled.div<{ variant: string }>`
  display: flex;
  gap: 16px;
  align-items: center;
  animation: ${fadeIn} 0.9s ease-out;
  
  ${props => props.variant === 'centered' && css`
    justify-content: center;
  `}
  
  @media (max-width: 480px) {
    flex-direction: column;
    width: 100%;
    
    & > * {
      width: 100%;
    }
  }
`;

const HeroImage = styled.div<{ position?: string }>`
  position: relative;
  animation: ${fadeIn} 1s ease-out;
  
  ${props => props.position !== 'background' && css`
    animation: ${float} 6s ease-in-out infinite;
  `}
  
  img {
    width: 100%;
    height: auto;
    border-radius: ${props => props.theme.spacing.radius['16']};
    box-shadow: ${props => props.theme.effects.shadows.low};
  }
`;

const BackgroundImage = styled.img`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 0;
`;

const GlowEffect = styled.div<{ theme: Theme }>`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 600px;
  height: 600px;
  background: radial-gradient(
    circle,
    ${props => props.theme.colors.brand.primary}30 0%,
    transparent 70%
  );
  filter: blur(100px);
  pointer-events: none;
  z-index: 0;
`;

export const Hero: React.FC<HeroProps> = ({
  title,
  subtitle,
  description,
  primaryAction,
  secondaryAction,
  image,
  variant = 'default',
  height = 'medium',
  backgroundEffect = 'none',
}) => {
  const isBackgroundImage = image?.position === 'background';
  const isSideImage = image && (image.position === 'left' || image.position === 'right');

  return (
    <HeroContainer 
      height={height} 
      hasBackgroundImage={isBackgroundImage} 
      variant={variant}
    >
      {isBackgroundImage && image && (
        <BackgroundImage src={image.src} alt={image.alt} />
      )}
      
      {backgroundEffect !== 'none' && (
        <BackgroundEffect effect={backgroundEffect} />
      )}
      
      {variant === 'gradient' && <GlowEffect />}
      
      <HeroContent variant={variant}>
        {isSideImage ? (
          <HeroGrid imagePosition={image?.position}>
            <HeroTextContent variant={variant}>
              {subtitle && <HeroSubtitle>{subtitle}</HeroSubtitle>}
              <HeroTitle variant={variant}>{title}</HeroTitle>
              {description && <HeroDescription>{description}</HeroDescription>}
              {(primaryAction || secondaryAction) && (
                <HeroActions variant={variant}>
                  {primaryAction}
                  {secondaryAction}
                </HeroActions>
              )}
            </HeroTextContent>
            
            <HeroImage position={image?.position}>
              <img src={image.src} alt={image.alt} />
            </HeroImage>
          </HeroGrid>
        ) : (
          <>
            <HeroTextContent variant={variant}>
              {subtitle && <HeroSubtitle>{subtitle}</HeroSubtitle>}
              <HeroTitle variant={variant}>{title}</HeroTitle>
              {description && <HeroDescription>{description}</HeroDescription>}
              {(primaryAction || secondaryAction) && (
                <HeroActions variant={variant}>
                  {primaryAction}
                  {secondaryAction}
                </HeroActions>
              )}
            </HeroTextContent>
          </>
        )}
      </HeroContent>
    </HeroContainer>
  );
};