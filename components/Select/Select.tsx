import React, { useState, useRef, useEffect } from 'react';
import styled, { css } from 'styled-components';
import { Theme } from '../../styles/theme';

export interface SelectOption {
  value: string | number;
  label: string;
  disabled?: boolean;
}

export interface SelectProps {
  options: SelectOption[];
  value?: string | number;
  defaultValue?: string | number;
  onChange?: (value: string | number) => void;
  placeholder?: string;
  label?: string;
  error?: string;
  helperText?: string;
  fullWidth?: boolean;
  disabled?: boolean;
}

const SelectWrapper = styled.div<{ fullWidth?: boolean }>`
  display: inline-flex;
  flex-direction: column;
  gap: 6px;
  position: relative;
  ${props => props.fullWidth && css`
    width: 100%;
  `}
`;

const Label = styled.label<{ theme: Theme }>`
  font-size: ${props => props.theme.typography.fontSize.small};
  font-weight: ${props => props.theme.typography.fontWeight.medium};
  color: ${props => props.theme.colors.text.secondary};
`;

const SelectTrigger = styled.button<{ 
  isOpen: boolean;
  hasError?: boolean;
  disabled?: boolean;
  theme: Theme;
}>`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 10px 12px;
  background: ${props => props.theme.colors.background.secondary};
  border: 1px solid ${props => props.hasError 
    ? props.theme.colors.status.red 
    : props.theme.colors.border.secondary};
  border-radius: ${props => props.theme.spacing.radius['8']};
  font-size: ${props => props.theme.typography.fontSize.regular};
  font-family: ${props => props.theme.typography.fontFamily.primary};
  color: ${props => props.theme.colors.text.primary};
  cursor: ${props => props.disabled ? 'not-allowed' : 'pointer'};
  transition: all ${props => props.theme.effects.transitions.quick} ease;
  text-align: left;

  &:hover:not(:disabled) {
    border-color: ${props => props.hasError 
      ? props.theme.colors.status.red 
      : props.theme.colors.border.tertiary};
  }

  ${props => props.isOpen && css`
    border-color: ${props.hasError 
      ? props.theme.colors.status.red 
      : props.theme.colors.brand.primary};
    background: ${props.theme.colors.background.primary};
    box-shadow: 0 0 0 3px ${props.hasError 
      ? `${props.theme.colors.status.red}20` 
      : `${props.theme.colors.brand.primary}20`};
  `}

  &:disabled {
    opacity: 0.5;
  }
`;

const SelectValue = styled.span<{ isPlaceholder: boolean; theme: Theme }>`
  flex: 1;
  color: ${props => props.isPlaceholder 
    ? props.theme.colors.text.quaternary 
    : props.theme.colors.text.primary};
`;

const ChevronIcon = styled.svg<{ isOpen: boolean }>`
  width: 16px;
  height: 16px;
  transition: transform 0.2s ease;
  transform: ${props => props.isOpen ? 'rotate(180deg)' : 'rotate(0)'};
`;

const DropdownMenu = styled.div<{ isOpen: boolean; theme: Theme }>`
  position: absolute;
  top: calc(100% + 4px);
  left: 0;
  right: 0;
  background: ${props => props.theme.colors.background.secondary};
  border: 1px solid ${props => props.theme.colors.border.secondary};
  border-radius: ${props => props.theme.spacing.radius['8']};
  box-shadow: ${props => props.theme.effects.shadows.low};
  max-height: 240px;
  overflow-y: auto;
  z-index: ${props => props.theme.zIndex.popover};
  opacity: ${props => props.isOpen ? 1 : 0};
  transform: ${props => props.isOpen ? 'translateY(0)' : 'translateY(-8px)'};
  pointer-events: ${props => props.isOpen ? 'auto' : 'none'};
  transition: all ${props => props.theme.effects.transitions.quick} ease;
`;

const Option = styled.button<{ 
  isSelected: boolean;
  disabled?: boolean;
  theme: Theme;
}>`
  display: block;
  width: 100%;
  padding: 8px 12px;
  background: ${props => props.isSelected 
    ? props.theme.colors.background.tertiary 
    : 'transparent'};
  border: none;
  text-align: left;
  font-size: ${props => props.theme.typography.fontSize.regular};
  font-family: ${props => props.theme.typography.fontFamily.primary};
  color: ${props => props.disabled 
    ? props.theme.colors.text.quaternary 
    : props.theme.colors.text.primary};
  cursor: ${props => props.disabled ? 'not-allowed' : 'pointer'};
  transition: background ${props => props.theme.effects.transitions.quick} ease;

  &:hover:not(:disabled) {
    background: ${props => props.theme.colors.background.tertiary};
  }

  &:disabled {
    opacity: 0.5;
  }
`;

const HelperText = styled.span<{ theme: Theme }>`
  font-size: ${props => props.theme.typography.fontSize.mini};
  color: ${props => props.theme.colors.text.tertiary};
`;

const ErrorText = styled.span<{ theme: Theme }>`
  font-size: ${props => props.theme.typography.fontSize.mini};
  color: ${props => props.theme.colors.status.red};
`;

export const Select: React.FC<SelectProps> = ({
  options,
  value,
  defaultValue,
  onChange,
  placeholder = 'Select an option',
  label,
  error,
  helperText,
  fullWidth = false,
  disabled = false,
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedValue, setSelectedValue] = useState(value || defaultValue);
  const selectRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (value !== undefined) {
      setSelectedValue(value);
    }
  }, [value]);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (selectRef.current && !selectRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleSelect = (optionValue: string | number) => {
    setSelectedValue(optionValue);
    setIsOpen(false);
    onChange?.(optionValue);
  };

  const selectedOption = options.find(opt => opt.value === selectedValue);

  return (
    <SelectWrapper ref={selectRef} fullWidth={fullWidth}>
      {label && <Label>{label}</Label>}
      <div style={{ position: 'relative' }}>
        <SelectTrigger
          type="button"
          isOpen={isOpen}
          hasError={!!error}
          disabled={disabled}
          onClick={() => !disabled && setIsOpen(!isOpen)}
        >
          <SelectValue isPlaceholder={!selectedOption}>
            {selectedOption ? selectedOption.label : placeholder}
          </SelectValue>
          <ChevronIcon isOpen={isOpen} viewBox="0 0 24 24" fill="none" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
          </ChevronIcon>
        </SelectTrigger>
        <DropdownMenu isOpen={isOpen}>
          {options.map((option) => (
            <Option
              key={option.value}
              isSelected={option.value === selectedValue}
              disabled={option.disabled}
              onClick={() => !option.disabled && handleSelect(option.value)}
            >
              {option.label}
            </Option>
          ))}
        </DropdownMenu>
      </div>
      {error && <ErrorText>{error}</ErrorText>}
      {helperText && !error && <HelperText>{helperText}</HelperText>}
    </SelectWrapper>
  );
};