import React, { forwardRef } from 'react';
import styled, { css } from 'styled-components';
import { Theme } from '../../styles/theme';

export interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
  helperText?: string;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  fullWidth?: boolean;
}

const InputWrapper = styled.div<{ fullWidth?: boolean }>`
  display: inline-flex;
  flex-direction: column;
  gap: 6px;
  ${props => props.fullWidth && css`
    width: 100%;
  `}
`;

const Label = styled.label<{ theme: Theme }>`
  font-size: ${props => props.theme.typography.fontSize.small};
  font-weight: ${props => props.theme.typography.fontWeight.medium};
  color: ${props => props.theme.colors.text.secondary};
`;

const InputContainer = styled.div<{ hasError?: boolean; theme: Theme }>`
  position: relative;
  display: flex;
  align-items: center;
  background: ${props => props.theme.colors.background.secondary};
  border: 1px solid ${props => props.hasError 
    ? props.theme.colors.status.red 
    : props.theme.colors.border.secondary};
  border-radius: ${props => props.theme.spacing.radius['8']};
  transition: all ${props => props.theme.effects.transitions.quick} ease;

  &:hover {
    border-color: ${props => props.hasError 
      ? props.theme.colors.status.red 
      : props.theme.colors.border.tertiary};
  }

  &:focus-within {
    border-color: ${props => props.hasError 
      ? props.theme.colors.status.red 
      : props.theme.colors.brand.primary};
    background: ${props => props.theme.colors.background.primary};
    box-shadow: 0 0 0 3px ${props => props.hasError 
      ? `${props.theme.colors.status.red}20` 
      : `${props.theme.colors.brand.primary}20`};
  }
`;

const StyledInput = styled.input<{ theme: Theme; hasLeftIcon?: boolean; hasRightIcon?: boolean }>`
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  padding: 10px 12px;
  font-size: ${props => props.theme.typography.fontSize.regular};
  font-family: ${props => props.theme.typography.fontFamily.primary};
  color: ${props => props.theme.colors.text.primary};
  
  ${props => props.hasLeftIcon && css`
    padding-left: 0;
  `}
  
  ${props => props.hasRightIcon && css`
    padding-right: 0;
  `}

  &::placeholder {
    color: ${props => props.theme.colors.text.quaternary};
  }

  &:disabled {
    cursor: not-allowed;
    opacity: 0.5;
  }
`;

const IconWrapper = styled.div<{ position: 'left' | 'right'; theme: Theme }>`
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 12px;
  color: ${props => props.theme.colors.text.tertiary};
  
  ${props => props.position === 'left' && css`
    padding-right: 0;
  `}
  
  ${props => props.position === 'right' && css`
    padding-left: 0;
  `}
`;

const HelperText = styled.span<{ theme: Theme }>`
  font-size: ${props => props.theme.typography.fontSize.mini};
  color: ${props => props.theme.colors.text.tertiary};
`;

const ErrorText = styled.span<{ theme: Theme }>`
  font-size: ${props => props.theme.typography.fontSize.mini};
  color: ${props => props.theme.colors.status.red};
`;

export const Input = forwardRef<HTMLInputElement, InputProps>(({
  label,
  error,
  helperText,
  leftIcon,
  rightIcon,
  fullWidth = false,
  id,
  ...props
}, ref) => {
  const inputId = id || `input-${Math.random().toString(36).substr(2, 9)}`;

  return (
    <InputWrapper fullWidth={fullWidth}>
      {label && <Label htmlFor={inputId}>{label}</Label>}
      <InputContainer hasError={!!error}>
        {leftIcon && <IconWrapper position="left">{leftIcon}</IconWrapper>}
        <StyledInput
          ref={ref}
          id={inputId}
          hasLeftIcon={!!leftIcon}
          hasRightIcon={!!rightIcon}
          {...props}
        />
        {rightIcon && <IconWrapper position="right">{rightIcon}</IconWrapper>}
      </InputContainer>
      {error && <ErrorText>{error}</ErrorText>}
      {helperText && !error && <HelperText>{helperText}</HelperText>}
    </InputWrapper>
  );
});

Input.displayName = 'Input';