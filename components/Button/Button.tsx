import React from 'react';
import styled, { css } from 'styled-components';
import { Theme } from '../../styles/theme';

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger';
  size?: 'small' | 'medium' | 'large';
  fullWidth?: boolean;
  isLoading?: boolean;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  children: React.ReactNode;
}

const buttonVariants = {
  primary: css<{ theme: Theme }>`
    background: ${props => props.theme.colors.brand.primary};
    color: ${props => props.theme.colors.brand.primaryText};
    border: 1px solid ${props => props.theme.colors.brand.primary};

    &:hover:not(:disabled) {
      background: ${props => props.theme.colors.brand.accent};
      border-color: ${props => props.theme.colors.brand.accent};
    }

    &:active:not(:disabled) {
      background: ${props => props.theme.colors.brand.accentHover};
      border-color: ${props => props.theme.colors.brand.accentHover};
    }
  `,
  secondary: css<{ theme: Theme }>`
    background: ${props => props.theme.colors.background.secondary};
    color: ${props => props.theme.colors.text.primary};
    border: 1px solid ${props => props.theme.colors.border.secondary};

    &:hover:not(:disabled) {
      background: ${props => props.theme.colors.background.tertiary};
      border-color: ${props => props.theme.colors.border.tertiary};
    }

    &:active:not(:disabled) {
      background: ${props => props.theme.colors.background.quaternary};
    }
  `,
  ghost: css<{ theme: Theme }>`
    background: transparent;
    color: ${props => props.theme.colors.text.secondary};
    border: 1px solid transparent;

    &:hover:not(:disabled) {
      color: ${props => props.theme.colors.text.primary};
      background: ${props => props.theme.colors.background.translucent};
    }

    &:active:not(:disabled) {
      background: ${props => props.theme.colors.background.secondary};
    }
  `,
  danger: css<{ theme: Theme }>`
    background: ${props => props.theme.colors.status.red};
    color: ${props => props.theme.colors.functional.white};
    border: 1px solid ${props => props.theme.colors.status.red};

    &:hover:not(:disabled) {
      background: #d14444;
      border-color: #d14444;
    }

    &:active:not(:disabled) {
      background: #b93333;
      border-color: #b93333;
    }
  `,
};

const buttonSizes = {
  small: css<{ theme: Theme }>`
    padding: 6px 12px;
    font-size: ${props => props.theme.typography.fontSize.small};
    border-radius: ${props => props.theme.spacing.radius['6']};
    height: 28px;
  `,
  medium: css<{ theme: Theme }>`
    padding: 8px 16px;
    font-size: ${props => props.theme.typography.fontSize.regular};
    border-radius: ${props => props.theme.spacing.radius['8']};
    height: 36px;
  `,
  large: css<{ theme: Theme }>`
    padding: 12px 24px;
    font-size: ${props => props.theme.typography.fontSize.large};
    border-radius: ${props => props.theme.spacing.radius['8']};
    height: 44px;
  `,
};

const StyledButton = styled.button<{
  variant: 'primary' | 'secondary' | 'ghost' | 'danger';
  size: 'small' | 'medium' | 'large';
  fullWidth?: boolean;
  isLoading?: boolean;
  theme: Theme;
}>`
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-family: ${props => props.theme.typography.fontFamily.primary};
  font-weight: ${props => props.theme.typography.fontWeight.medium};
  line-height: 1;
  white-space: nowrap;
  transition: all ${props => props.theme.effects.transitions.quick} ease;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  user-select: none;
  outline: none;

  ${props => buttonVariants[props.variant]}
  ${props => buttonSizes[props.size]}

  ${props => props.fullWidth && css`
    width: 100%;
  `}

  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  &:focus-visible {
    box-shadow: 0 0 0 2px ${props => props.theme.colors.brand.accent};
  }

  ${props => props.isLoading && css`
    color: transparent;
    pointer-events: none;

    &::after {
      content: '';
      position: absolute;
      width: 16px;
      height: 16px;
      top: 50%;
      left: 50%;
      margin-left: -8px;
      margin-top: -8px;
      border: 2px solid ${props.variant === 'primary' || props.variant === 'danger' 
        ? props.theme.colors.functional.white 
        : props.theme.colors.text.secondary};
      border-radius: 50%;
      border-top-color: transparent;
      animation: spinner 0.6s linear infinite;
    }
  `}

  @keyframes spinner {
    to {
      transform: rotate(360deg);
    }
  }
`;

const IconWrapper = styled.span`
  display: inline-flex;
  align-items: center;
  justify-content: center;
`;

export const Button: React.FC<ButtonProps> = ({
  variant = 'primary',
  size = 'medium',
  fullWidth = false,
  isLoading = false,
  leftIcon,
  rightIcon,
  children,
  disabled,
  ...props
}) => {
  return (
    <StyledButton
      variant={variant}
      size={size}
      fullWidth={fullWidth}
      isLoading={isLoading}
      disabled={disabled || isLoading}
      {...props}
    >
      {leftIcon && <IconWrapper>{leftIcon}</IconWrapper>}
      {children}
      {rightIcon && <IconWrapper>{rightIcon}</IconWrapper>}
    </StyledButton>
  );
};