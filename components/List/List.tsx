import React from 'react';
import styled, { css } from 'styled-components';
import { Theme } from '../../styles/theme';
import { Badge } from '../Badge';
import { Avatar } from '../Avatar';
import { Button } from '../Button';

export interface ListItemAction {
  label: string;
  icon?: React.ReactNode;
  onClick: (item: any) => void;
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger';
}

export interface ListColumn {
  key: string;
  label: string;
  width?: string;
  align?: 'left' | 'center' | 'right';
  render?: (value: any, item: any) => React.ReactNode;
  sortable?: boolean;
}

export interface ListProps {
  data: any[];
  columns?: ListColumn[];
  variant?: 'default' | 'striped' | 'bordered' | 'cards';
  size?: 'small' | 'medium' | 'large';
  selectable?: boolean;
  selectedItems?: any[];
  onSelectionChange?: (items: any[]) => void;
  actions?: ListItemAction[];
  onItemClick?: (item: any) => void;
  emptyMessage?: string;
  loading?: boolean;
  hoverable?: boolean;
  sortBy?: string;
  sortDirection?: 'asc' | 'desc';
  onSort?: (key: string) => void;
}

const ListContainer = styled.div<{ variant: string; theme: Theme }>`
  width: 100%;
  ${props => props.variant === 'cards' && css`
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 16px;
  `}
`;

const Table = styled.table<{ variant: string; theme: Theme }>`
  width: 100%;
  border-collapse: collapse;
  
  ${props => props.variant === 'bordered' && css`
    border: 1px solid ${props.theme.colors.border.primary};
    border-radius: ${props.theme.spacing.radius['8']};
    overflow: hidden;
  `}
`;

const TableHead = styled.thead<{ theme: Theme }>`
  background: ${props => props.theme.colors.background.secondary};
  border-bottom: 1px solid ${props => props.theme.colors.border.primary};
`;

const TableBody = styled.tbody``;

const TableRow = styled.tr<{ 
  variant: string;
  hoverable?: boolean;
  clickable?: boolean;
  selected?: boolean;
  theme: Theme;
}>`
  border-bottom: 1px solid ${props => props.theme.colors.border.primary};
  transition: background ${props => props.theme.effects.transitions.quick} ease;
  
  ${props => props.variant === 'striped' && css`
    &:nth-child(even) {
      background: ${props.theme.colors.background.translucent};
    }
  `}
  
  ${props => props.selected && css`
    background: ${props.theme.colors.brand.primary}10;
  `}
  
  ${props => (props.hoverable || props.clickable) && css`
    &:hover {
      background: ${props.theme.colors.background.secondary};
      cursor: ${props.clickable ? 'pointer' : 'default'};
    }
  `}
  
  &:last-child {
    border-bottom: none;
  }
`;

const TableHeader = styled.th<{ 
  align?: string;
  width?: string;
  sortable?: boolean;
  size: string;
  theme: Theme;
}>`
  padding: ${props => 
    props.size === 'small' ? '8px 12px' :
    props.size === 'large' ? '16px 20px' :
    '12px 16px'
  };
  text-align: ${props => props.align || 'left'};
  width: ${props => props.width || 'auto'};
  font-size: ${props => props.theme.typography.fontSize.small};
  font-weight: ${props => props.theme.typography.fontWeight.semibold};
  color: ${props => props.theme.colors.text.secondary};
  text-transform: uppercase;
  letter-spacing: 0.05em;
  user-select: none;
  
  ${props => props.sortable && css`
    cursor: pointer;
    transition: color ${props.theme.effects.transitions.quick} ease;
    
    &:hover {
      color: ${props.theme.colors.text.primary};
    }
  `}
`;

const SortIcon = styled.span<{ direction?: 'asc' | 'desc'; active?: boolean; theme: Theme }>`
  display: inline-block;
  margin-left: 4px;
  opacity: ${props => props.active ? 1 : 0.3};
  transition: opacity ${props => props.theme.effects.transitions.quick} ease;
  
  ${TableHeader}:hover & {
    opacity: 0.7;
  }
`;

const TableCell = styled.td<{ 
  align?: string;
  size: string;
  theme: Theme;
}>`
  padding: ${props => 
    props.size === 'small' ? '8px 12px' :
    props.size === 'large' ? '16px 20px' :
    '12px 16px'
  };
  text-align: ${props => props.align || 'left'};
  font-size: ${props => props.theme.typography.fontSize.regular};
  color: ${props => props.theme.colors.text.primary};
`;

const Checkbox = styled.input<{ theme: Theme }>`
  width: 16px;
  height: 16px;
  cursor: pointer;
  accent-color: ${props => props.theme.colors.brand.primary};
`;

const ActionsCell = styled.div`
  display: flex;
  gap: 8px;
  justify-content: flex-end;
`;

const ListCard = styled.div<{ 
  hoverable?: boolean;
  clickable?: boolean;
  selected?: boolean;
  theme: Theme;
}>`
  background: ${props => props.theme.colors.background.secondary};
  border: 1px solid ${props => props.theme.colors.border.primary};
  border-radius: ${props => props.theme.spacing.radius['12']};
  padding: 20px;
  transition: all ${props => props.theme.effects.transitions.regular} ease;
  
  ${props => props.selected && css`
    border-color: ${props.theme.colors.brand.primary};
    background: ${props.theme.colors.brand.primary}10;
  `}
  
  ${props => (props.hoverable || props.clickable) && css`
    cursor: ${props.clickable ? 'pointer' : 'default'};
    
    &:hover {
      transform: translateY(-2px);
      border-color: ${props.theme.colors.border.secondary};
      box-shadow: ${props.theme.effects.shadows.low};
    }
  `}
`;

const CardHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
`;

const CardTitle = styled.h3`
  font-size: ${props => props.theme.typography.fontSize.large};
  font-weight: ${props => props.theme.typography.fontWeight.semibold};
  color: ${props => props.theme.colors.text.primary};
  margin: 0;
`;

const CardContent = styled.div`
  display: flex;
  flex-direction: column;
  gap: 12px;
`;

const CardField = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const CardLabel = styled.span`
  font-size: ${props => props.theme.typography.fontSize.small};
  color: ${props => props.theme.colors.text.tertiary};
`;

const CardValue = styled.span`
  font-size: ${props => props.theme.typography.fontSize.regular};
  color: ${props => props.theme.colors.text.primary};
`;

const EmptyState = styled.div`
  padding: 48px;
  text-align: center;
  color: ${props => props.theme.colors.text.tertiary};
  font-size: ${props => props.theme.typography.fontSize.regular};
`;

const LoadingOverlay = styled.div`
  position: relative;
  
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: ${props => props.theme.colors.background.primary}99;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: ${props => props.theme.spacing.radius['8']};
  }
`;

const Spinner = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 32px;
  height: 32px;
  border: 3px solid ${props => props.theme.colors.border.secondary};
  border-top-color: ${props => props.theme.colors.brand.primary};
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  z-index: 1;
  
  @keyframes spin {
    to {
      transform: translate(-50%, -50%) rotate(360deg);
    }
  }
`;

export const List: React.FC<ListProps> = ({
  data,
  columns = [],
  variant = 'default',
  size = 'medium',
  selectable = false,
  selectedItems = [],
  onSelectionChange,
  actions = [],
  onItemClick,
  emptyMessage = 'No items to display',
  loading = false,
  hoverable = true,
  sortBy,
  sortDirection = 'asc',
  onSort,
}) => {
  const handleSelectAll = (checked: boolean) => {
    if (onSelectionChange) {
      onSelectionChange(checked ? [...data] : []);
    }
  };

  const handleSelectItem = (item: any, checked: boolean) => {
    if (onSelectionChange) {
      if (checked) {
        onSelectionChange([...selectedItems, item]);
      } else {
        onSelectionChange(selectedItems.filter(i => i !== item));
      }
    }
  };

  const isSelected = (item: any) => selectedItems.includes(item);

  const renderTableView = () => (
    <Table variant={variant}>
      <TableHead>
        <TableRow variant={variant}>
          {selectable && (
            <TableHeader size={size} style={{ width: '40px' }}>
              <Checkbox
                type="checkbox"
                checked={selectedItems.length === data.length && data.length > 0}
                onChange={(e) => handleSelectAll(e.target.checked)}
              />
            </TableHeader>
          )}
          {columns.map((column) => (
            <TableHeader
              key={column.key}
              align={column.align}
              width={column.width}
              size={size}
              sortable={column.sortable}
              onClick={() => column.sortable && onSort && onSort(column.key)}
            >
              {column.label}
              {column.sortable && (
                <SortIcon active={sortBy === column.key} direction={sortDirection}>
                  {sortBy === column.key ? (
                    sortDirection === 'asc' ? '↑' : '↓'
                  ) : '↕'}
                </SortIcon>
              )}
            </TableHeader>
          ))}
          {actions.length > 0 && (
            <TableHeader size={size} align="right">Actions</TableHeader>
          )}
        </TableRow>
      </TableHead>
      <TableBody>
        {data.map((item, index) => (
          <TableRow
            key={index}
            variant={variant}
            hoverable={hoverable}
            clickable={!!onItemClick}
            selected={isSelected(item)}
            onClick={() => onItemClick && onItemClick(item)}
          >
            {selectable && (
              <TableCell size={size} style={{ width: '40px' }}>
                <Checkbox
                  type="checkbox"
                  checked={isSelected(item)}
                  onChange={(e) => handleSelectItem(item, e.target.checked)}
                  onClick={(e) => e.stopPropagation()}
                />
              </TableCell>
            )}
            {columns.map((column) => (
              <TableCell key={column.key} align={column.align} size={size}>
                {column.render 
                  ? column.render(item[column.key], item)
                  : item[column.key]
                }
              </TableCell>
            ))}
            {actions.length > 0 && (
              <TableCell size={size}>
                <ActionsCell>
                  {actions.map((action, actionIndex) => (
                    <Button
                      key={actionIndex}
                      variant={action.variant || 'ghost'}
                      size="small"
                      onClick={(e) => {
                        e.stopPropagation();
                        action.onClick(item);
                      }}
                    >
                      {action.icon}
                      {action.label}
                    </Button>
                  ))}
                </ActionsCell>
              </TableCell>
            )}
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );

  const renderCardView = () => (
    <ListContainer variant="cards">
      {data.map((item, index) => (
        <ListCard
          key={index}
          hoverable={hoverable}
          clickable={!!onItemClick}
          selected={isSelected(item)}
          onClick={() => onItemClick && onItemClick(item)}
        >
          <CardHeader>
            <CardTitle>
              {columns[0] && (columns[0].render 
                ? columns[0].render(item[columns[0].key], item)
                : item[columns[0].key]
              )}
            </CardTitle>
            {selectable && (
              <Checkbox
                type="checkbox"
                checked={isSelected(item)}
                onChange={(e) => handleSelectItem(item, e.target.checked)}
                onClick={(e) => e.stopPropagation()}
              />
            )}
          </CardHeader>
          <CardContent>
            {columns.slice(1).map((column) => (
              <CardField key={column.key}>
                <CardLabel>{column.label}</CardLabel>
                <CardValue>
                  {column.render 
                    ? column.render(item[column.key], item)
                    : item[column.key]
                  }
                </CardValue>
              </CardField>
            ))}
          </CardContent>
          {actions.length > 0 && (
            <ActionsCell style={{ marginTop: '16px' }}>
              {actions.map((action, actionIndex) => (
                <Button
                  key={actionIndex}
                  variant={action.variant || 'ghost'}
                  size="small"
                  fullWidth
                  onClick={(e) => {
                    e.stopPropagation();
                    action.onClick(item);
                  }}
                >
                  {action.icon}
                  {action.label}
                </Button>
              ))}
            </ActionsCell>
          )}
        </ListCard>
      ))}
    </ListContainer>
  );

  if (data.length === 0 && !loading) {
    return <EmptyState>{emptyMessage}</EmptyState>;
  }

  const content = variant === 'cards' ? renderCardView() : renderTableView();

  if (loading) {
    return (
      <LoadingOverlay>
        {content}
        <Spinner />
      </LoadingOverlay>
    );
  }

  return content;
};