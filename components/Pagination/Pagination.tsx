import React, { useMemo } from 'react';
import styled, { css } from 'styled-components';
import { Theme } from '../../styles/theme';

export interface PaginationProps {
  currentPage: number;
  totalPages: number;
  totalItems?: number;
  itemsPerPage?: number;
  onPageChange: (page: number) => void;
  showFirstLast?: boolean;
  showPrevNext?: boolean;
  showPageInfo?: boolean;
  showItemsPerPage?: boolean;
  itemsPerPageOptions?: number[];
  onItemsPerPageChange?: (itemsPerPage: number) => void;
  maxVisiblePages?: number;
  variant?: 'default' | 'compact' | 'minimal';
  size?: 'small' | 'medium' | 'large';
}

const PaginationContainer = styled.div<{ variant: string; theme: Theme }>`
  display: flex;
  align-items: center;
  gap: ${props => props.variant === 'compact' ? '8px' : '16px'};
  flex-wrap: wrap;
  
  ${props => props.variant === 'minimal' && css`
    justify-content: center;
  `}
`;

const PageButtonsWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 4px;
`;

const PageButton = styled.button<{ 
  isActive?: boolean;
  disabled?: boolean;
  size: string;
  theme: Theme;
}>`
  min-width: ${props => 
    props.size === 'small' ? '28px' :
    props.size === 'large' ? '40px' :
    '32px'
  };
  height: ${props => 
    props.size === 'small' ? '28px' :
    props.size === 'large' ? '40px' :
    '32px'
  };
  padding: 0 8px;
  background: ${props => props.isActive 
    ? props.theme.colors.brand.primary 
    : props.theme.colors.background.secondary};
  color: ${props => props.isActive 
    ? props.theme.colors.brand.primaryText 
    : props.theme.colors.text.secondary};
  border: 1px solid ${props => props.isActive 
    ? props.theme.colors.brand.primary 
    : props.theme.colors.border.secondary};
  border-radius: ${props => props.theme.spacing.radius['6']};
  font-size: ${props => 
    props.size === 'small' ? props.theme.typography.fontSize.mini :
    props.size === 'large' ? props.theme.typography.fontSize.regular :
    props.theme.typography.fontSize.small
  };
  font-weight: ${props => props.theme.typography.fontWeight.medium};
  cursor: ${props => props.disabled ? 'not-allowed' : 'pointer'};
  transition: all ${props => props.theme.effects.transitions.quick} ease;
  display: flex;
  align-items: center;
  justify-content: center;
  
  &:disabled {
    opacity: 0.5;
  }
  
  &:not(:disabled):not(.active):hover {
    background: ${props => props.theme.colors.background.tertiary};
    border-color: ${props => props.theme.colors.border.tertiary};
    color: ${props => props.theme.colors.text.primary};
  }
`;

const NavigationButton = styled(PageButton)`
  padding: 0 12px;
  width: auto;
`;

const Ellipsis = styled.span<{ theme: Theme }>`
  padding: 0 8px;
  color: ${props => props.theme.colors.text.quaternary};
  user-select: none;
`;

const PageInfo = styled.div<{ theme: Theme }>`
  font-size: ${props => props.theme.typography.fontSize.small};
  color: ${props => props.theme.colors.text.tertiary};
  white-space: nowrap;
`;

const ItemsPerPageSelector = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
`;

const Label = styled.label<{ theme: Theme }>`
  font-size: ${props => props.theme.typography.fontSize.small};
  color: ${props => props.theme.colors.text.tertiary};
  white-space: nowrap;
`;

const Select = styled.select<{ size: string; theme: Theme }>`
  padding: ${props => 
    props.size === 'small' ? '4px 8px' :
    props.size === 'large' ? '8px 12px' :
    '6px 10px'
  };
  background: ${props => props.theme.colors.background.secondary};
  border: 1px solid ${props => props.theme.colors.border.secondary};
  border-radius: ${props => props.theme.spacing.radius['6']};
  color: ${props => props.theme.colors.text.primary};
  font-size: ${props => 
    props.size === 'small' ? props.theme.typography.fontSize.mini :
    props.size === 'large' ? props.theme.typography.fontSize.regular :
    props.theme.typography.fontSize.small
  };
  cursor: pointer;
  transition: all ${props => props.theme.effects.transitions.quick} ease;
  
  &:hover {
    border-color: ${props => props.theme.colors.border.tertiary};
  }
  
  &:focus {
    outline: none;
    border-color: ${props => props.theme.colors.brand.primary};
    box-shadow: 0 0 0 3px ${props => props.theme.colors.brand.primary}20;
  }
`;

const QuickJump = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
`;

const JumpInput = styled.input<{ size: string; theme: Theme }>`
  width: 60px;
  padding: ${props => 
    props.size === 'small' ? '4px 8px' :
    props.size === 'large' ? '8px 12px' :
    '6px 10px'
  };
  background: ${props => props.theme.colors.background.secondary};
  border: 1px solid ${props => props.theme.colors.border.secondary};
  border-radius: ${props => props.theme.spacing.radius['6']};
  color: ${props => props.theme.colors.text.primary};
  font-size: ${props => 
    props.size === 'small' ? props.theme.typography.fontSize.mini :
    props.size === 'large' ? props.theme.typography.fontSize.regular :
    props.theme.typography.fontSize.small
  };
  text-align: center;
  transition: all ${props => props.theme.effects.transitions.quick} ease;
  
  &:hover {
    border-color: ${props => props.theme.colors.border.tertiary};
  }
  
  &:focus {
    outline: none;
    border-color: ${props => props.theme.colors.brand.primary};
    box-shadow: 0 0 0 3px ${props => props.theme.colors.brand.primary}20;
  }
  
  &::-webkit-inner-spin-button,
  &::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
`;

export const Pagination: React.FC<PaginationProps> = ({
  currentPage,
  totalPages,
  totalItems,
  itemsPerPage = 10,
  onPageChange,
  showFirstLast = true,
  showPrevNext = true,
  showPageInfo = false,
  showItemsPerPage = false,
  itemsPerPageOptions = [10, 20, 50, 100],
  onItemsPerPageChange,
  maxVisiblePages = 7,
  variant = 'default',
  size = 'medium',
}) => {
  const pageNumbers = useMemo(() => {
    const pages: (number | string)[] = [];
    
    if (totalPages <= maxVisiblePages) {
      for (let i = 1; i <= totalPages; i++) {
        pages.push(i);
      }
    } else {
      const halfVisible = Math.floor(maxVisiblePages / 2);
      const startPage = Math.max(1, currentPage - halfVisible);
      const endPage = Math.min(totalPages, currentPage + halfVisible);
      
      if (startPage > 1) {
        pages.push(1);
        if (startPage > 2) {
          pages.push('...');
        }
      }
      
      for (let i = startPage; i <= endPage; i++) {
        if (i !== 1 && i !== totalPages) {
          pages.push(i);
        }
      }
      
      if (endPage < totalPages) {
        if (endPage < totalPages - 1) {
          pages.push('...');
        }
        pages.push(totalPages);
      }
    }
    
    return pages;
  }, [currentPage, totalPages, maxVisiblePages]);

  const handlePageChange = (page: number) => {
    if (page >= 1 && page <= totalPages && page !== currentPage) {
      onPageChange(page);
    }
  };

  const handleJumpToPage = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') {
      const page = parseInt(event.currentTarget.value);
      if (!isNaN(page)) {
        handlePageChange(page);
      }
    }
  };

  if (totalPages <= 1 && variant === 'minimal') {
    return null;
  }

  const startItem = totalItems ? (currentPage - 1) * itemsPerPage + 1 : 0;
  const endItem = totalItems ? Math.min(currentPage * itemsPerPage, totalItems) : 0;

  return (
    <PaginationContainer variant={variant}>
      {showPageInfo && totalItems && variant !== 'minimal' && (
        <PageInfo>
          Showing {startItem}-{endItem} of {totalItems} items
        </PageInfo>
      )}
      
      {showItemsPerPage && onItemsPerPageChange && variant === 'default' && (
        <ItemsPerPageSelector>
          <Label>Items per page:</Label>
          <Select 
            size={size}
            value={itemsPerPage}
            onChange={(e) => onItemsPerPageChange(parseInt(e.target.value))}
          >
            {itemsPerPageOptions.map(option => (
              <option key={option} value={option}>{option}</option>
            ))}
          </Select>
        </ItemsPerPageSelector>
      )}
      
      <PageButtonsWrapper>
        {showFirstLast && variant !== 'minimal' && (
          <NavigationButton
            size={size}
            disabled={currentPage === 1}
            onClick={() => handlePageChange(1)}
            title="First page"
          >
            ⟨⟨
          </NavigationButton>
        )}
        
        {showPrevNext && (
          <NavigationButton
            size={size}
            disabled={currentPage === 1}
            onClick={() => handlePageChange(currentPage - 1)}
            title="Previous page"
          >
            {variant === 'minimal' ? 'Previous' : '⟨'}
          </NavigationButton>
        )}
        
        {variant !== 'minimal' && pageNumbers.map((page, index) => {
          if (page === '...') {
            return <Ellipsis key={`ellipsis-${index}`}>...</Ellipsis>;
          }
          
          return (
            <PageButton
              key={page}
              size={size}
              isActive={page === currentPage}
              onClick={() => handlePageChange(page as number)}
              title={`Page ${page}`}
            >
              {page}
            </PageButton>
          );
        })}
        
        {variant === 'minimal' && (
          <PageInfo style={{ padding: '0 16px' }}>
            Page {currentPage} of {totalPages}
          </PageInfo>
        )}
        
        {showPrevNext && (
          <NavigationButton
            size={size}
            disabled={currentPage === totalPages}
            onClick={() => handlePageChange(currentPage + 1)}
            title="Next page"
          >
            {variant === 'minimal' ? 'Next' : '⟩'}
          </NavigationButton>
        )}
        
        {showFirstLast && variant !== 'minimal' && (
          <NavigationButton
            size={size}
            disabled={currentPage === totalPages}
            onClick={() => handlePageChange(totalPages)}
            title="Last page"
          >
            ⟩⟩
          </NavigationButton>
        )}
      </PageButtonsWrapper>
      
      {variant === 'default' && (
        <QuickJump>
          <Label>Go to:</Label>
          <JumpInput
            size={size}
            type="number"
            min="1"
            max={totalPages}
            placeholder={String(currentPage)}
            onKeyDown={handleJumpToPage}
          />
        </QuickJump>
      )}
    </PaginationContainer>
  );
};