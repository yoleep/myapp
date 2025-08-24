import React from 'react';
import styled, { css } from 'styled-components';
import { Theme } from '../../styles/theme';

export interface CardProps {
  children: React.ReactNode;
  variant?: 'default' | 'elevated' | 'outlined';
  padding?: 'none' | 'small' | 'medium' | 'large';
  fullWidth?: boolean;
  onClick?: () => void;
  className?: string;
  image?: {
    src: string;
    alt: string;
    position?: 'top' | 'left' | 'right' | 'cover';
    height?: string;
  };
  title?: string;
  subtitle?: string;
}

const cardVariants = {
  default: css<{ theme: Theme }>`
    background: ${props => props.theme.colors.background.secondary};
    border: 1px solid ${props => props.theme.colors.border.primary};
  `,
  elevated: css<{ theme: Theme }>`
    background: ${props => props.theme.colors.background.secondary};
    border: 1px solid ${props => props.theme.colors.border.primary};
    box-shadow: ${props => props.theme.effects.shadows.low};
  `,
  outlined: css<{ theme: Theme }>`
    background: transparent;
    border: 1px solid ${props => props.theme.colors.border.secondary};
  `,
};

const cardPadding = {
  none: css`
    padding: 0;
  `,
  small: css`
    padding: 12px;
  `,
  medium: css`
    padding: 20px;
  `,
  large: css`
    padding: 32px;
  `,
};

const StyledCard = styled.div<{
  variant: 'default' | 'elevated' | 'outlined';
  padding: 'none' | 'small' | 'medium' | 'large';
  fullWidth?: boolean;
  clickable?: boolean;
  theme: Theme;
}>`
  border-radius: ${props => props.theme.spacing.radius['12']};
  transition: all ${props => props.theme.effects.transitions.regular} ease;
  
  ${props => cardVariants[props.variant]}
  ${props => cardPadding[props.padding]}
  
  ${props => props.fullWidth && css`
    width: 100%;
  `}
  
  ${props => props.clickable && css`
    cursor: pointer;
    
    &:hover {
      background: ${props.theme.colors.background.tertiary};
      border-color: ${props.theme.colors.border.tertiary};
      transform: translateY(-2px);
    }
    
    &:active {
      transform: translateY(0);
    }
  `}
`;

const CardImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const CardImageWrapper = styled.div<{ 
  position: 'top' | 'left' | 'right' | 'cover';
  height?: string;
}>`
  ${props => props.position === 'top' && css`
    width: 100%;
    height: ${props.height || '200px'};
    margin: -20px -20px 20px -20px;
    border-radius: 12px 12px 0 0;
    overflow: hidden;
  `}
  
  ${props => props.position === 'left' && css`
    width: 40%;
    min-width: 150px;
    height: ${props.height || '100%'};
    margin: -20px 20px -20px -20px;
    border-radius: 12px 0 0 12px;
    overflow: hidden;
  `}
  
  ${props => props.position === 'right' && css`
    width: 40%;
    min-width: 150px;
    height: ${props.height || '100%'};
    margin: -20px -20px -20px 20px;
    border-radius: 0 12px 12px 0;
    overflow: hidden;
  `}
  
  ${props => props.position === 'cover' && css`
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    border-radius: 12px;
    overflow: hidden;
    z-index: 0;
  `}
`;

const CardContent = styled.div<{ hasImage?: boolean; imagePosition?: string }>`
  ${props => props.hasImage && props.imagePosition === 'cover' && css`
    position: relative;
    z-index: 1;
    background: linear-gradient(to top, 
      ${props.theme.colors.background.primary}ee 0%, 
      ${props.theme.colors.background.primary}99 60%, 
      transparent 100%);
    padding: 40px 20px 20px;
    margin: -20px;
    min-height: 200px;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    border-radius: 12px;
  `}
`;

const CardWrapper = styled.div<{ 
  hasImage?: boolean; 
  imagePosition?: string;
}>`
  ${props => props.hasImage && (props.imagePosition === 'left' || props.imagePosition === 'right') && css`
    display: flex;
    align-items: stretch;
    ${props.imagePosition === 'right' && css`
      flex-direction: row-reverse;
    `}
  `}
  
  ${props => props.hasImage && props.imagePosition === 'cover' && css`
    position: relative;
  `}
`;

const CardTitle = styled.h3<{ theme: Theme }>`
  font-size: ${props => props.theme.typography.fontSize.large};
  font-weight: ${props => props.theme.typography.fontWeight.semibold};
  color: ${props => props.theme.colors.text.primary};
  margin-bottom: 8px;
`;

const CardSubtitle = styled.p<{ theme: Theme }>`
  font-size: ${props => props.theme.typography.fontSize.regular};
  color: ${props => props.theme.colors.text.secondary};
  margin-bottom: 16px;
`;

export const Card: React.FC<CardProps> = ({
  children,
  variant = 'default',
  padding = 'medium',
  fullWidth = false,
  onClick,
  className,
  image,
  title,
  subtitle,
}) => {
  const imagePosition = image?.position || 'top';
  const hasImage = !!image;
  const shouldWrapContent = hasImage && (imagePosition === 'left' || imagePosition === 'right' || imagePosition === 'cover');

  const content = (
    <>
      {title && <CardTitle>{title}</CardTitle>}
      {subtitle && <CardSubtitle>{subtitle}</CardSubtitle>}
      {children}
    </>
  );

  return (
    <StyledCard
      variant={variant}
      padding={padding}
      fullWidth={fullWidth}
      clickable={!!onClick}
      onClick={onClick}
      className={className}
    >
      {shouldWrapContent ? (
        <CardWrapper hasImage={hasImage} imagePosition={imagePosition}>
          {image && (
            <CardImageWrapper position={imagePosition} height={image.height}>
              <CardImage src={image.src} alt={image.alt} />
            </CardImageWrapper>
          )}
          <CardContent hasImage={hasImage} imagePosition={imagePosition}>
            {content}
          </CardContent>
        </CardWrapper>
      ) : (
        <>
          {image && imagePosition === 'top' && (
            <CardImageWrapper position={imagePosition} height={image.height}>
              <CardImage src={image.src} alt={image.alt} />
            </CardImageWrapper>
          )}
          {content}
        </>
      )}
    </StyledCard>
  );
};