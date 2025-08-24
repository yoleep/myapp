import React from 'react';
import styled, { css } from 'styled-components';
import { Theme } from '../../styles/theme';

export interface BadgeProps {
  children: React.ReactNode;
  variant?: 'default' | 'primary' | 'success' | 'warning' | 'danger' | 'info';
  size?: 'small' | 'medium';
  dot?: boolean;
}

const badgeVariants = {
  default: css<{ theme: Theme }>`
    background: ${props => props.theme.colors.background.tertiary};
    color: ${props => props.theme.colors.text.secondary};
    border: 1px solid ${props => props.theme.colors.border.secondary};
  `,
  primary: css<{ theme: Theme }>`
    background: ${props => props.theme.colors.brand.primary}20;
    color: ${props => props.theme.colors.brand.accent};
    border: 1px solid ${props => props.theme.colors.brand.primary}30;
  `,
  success: css<{ theme: Theme }>`
    background: ${props => props.theme.colors.status.green}20;
    color: ${props => props.theme.colors.status.green};
    border: 1px solid ${props => props.theme.colors.status.green}30;
  `,
  warning: css<{ theme: Theme }>`
    background: ${props => props.theme.colors.status.orange}20;
    color: ${props => props.theme.colors.status.orange};
    border: 1px solid ${props => props.theme.colors.status.orange}30;
  `,
  danger: css<{ theme: Theme }>`
    background: ${props => props.theme.colors.status.red}20;
    color: ${props => props.theme.colors.status.red};
    border: 1px solid ${props => props.theme.colors.status.red}30;
  `,
  info: css<{ theme: Theme }>`
    background: ${props => props.theme.colors.brand.blue}20;
    color: ${props => props.theme.colors.brand.blue};
    border: 1px solid ${props => props.theme.colors.brand.blue}30;
  `,
};

const badgeSizes = {
  small: css<{ theme: Theme }>`
    padding: 2px 6px;
    font-size: ${props => props.theme.typography.fontSize.micro};
  `,
  medium: css<{ theme: Theme }>`
    padding: 4px 8px;
    font-size: ${props => props.theme.typography.fontSize.mini};
  `,
};

const StyledBadge = styled.span<{
  variant: 'default' | 'primary' | 'success' | 'warning' | 'danger' | 'info';
  size: 'small' | 'medium';
  theme: Theme;
}>`
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-family: ${props => props.theme.typography.fontFamily.primary};
  font-weight: ${props => props.theme.typography.fontWeight.medium};
  line-height: 1;
  border-radius: ${props => props.theme.spacing.radius['4']};
  white-space: nowrap;
  
  ${props => badgeVariants[props.variant]}
  ${props => badgeSizes[props.size]}
`;

const Dot = styled.span<{ variant: string; theme: Theme }>`
  width: 6px;
  height: 6px;
  border-radius: ${props => props.theme.spacing.radius.circle};
  background: currentColor;
`;

export const Badge: React.FC<BadgeProps> = ({
  children,
  variant = 'default',
  size = 'medium',
  dot = false,
}) => {
  return (
    <StyledBadge variant={variant} size={size}>
      {dot && <Dot variant={variant} />}
      {children}
    </StyledBadge>
  );
};