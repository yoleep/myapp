import React from 'react';
import styled, { css } from 'styled-components';
import { Theme } from '../../styles/theme';

export interface AvatarProps {
  src?: string;
  alt?: string;
  name?: string;
  size?: 'small' | 'medium' | 'large' | 'xlarge';
  shape?: 'circle' | 'square';
  status?: 'online' | 'offline' | 'busy' | 'away';
}

const avatarSizes = {
  small: css`
    width: 24px;
    height: 24px;
    font-size: 10px;
  `,
  medium: css`
    width: 32px;
    height: 32px;
    font-size: 13px;
  `,
  large: css`
    width: 40px;
    height: 40px;
    font-size: 16px;
  `,
  xlarge: css`
    width: 64px;
    height: 64px;
    font-size: 24px;
  `,
};

const statusColors = {
  online: '#4cb782',
  offline: '#62666d',
  busy: '#eb5757',
  away: '#fc7840',
};

const AvatarContainer = styled.div<{ size: string }>`
  position: relative;
  display: inline-block;
  ${props => avatarSizes[props.size as keyof typeof avatarSizes]}
`;

const StyledAvatar = styled.div<{
  shape: 'circle' | 'square';
  hasImage: boolean;
  theme: Theme;
}>`
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: ${props => props.hasImage ? 'transparent' : props.theme.colors.background.tertiary};
  color: ${props => props.theme.colors.text.secondary};
  font-weight: ${props => props.theme.typography.fontWeight.medium};
  border-radius: ${props => props.shape === 'circle' 
    ? props.theme.spacing.radius.circle 
    : props.theme.spacing.radius['8']};
  overflow: hidden;
  user-select: none;
  border: 2px solid ${props => props.theme.colors.border.primary};
`;

const AvatarImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const StatusIndicator = styled.div<{ 
  status: 'online' | 'offline' | 'busy' | 'away';
  size: string;
  theme: Theme;
}>`
  position: absolute;
  bottom: 0;
  right: 0;
  width: ${props => props.size === 'small' ? '8px' : props.size === 'medium' ? '10px' : '12px'};
  height: ${props => props.size === 'small' ? '8px' : props.size === 'medium' ? '10px' : '12px'};
  background: ${props => statusColors[props.status]};
  border: 2px solid ${props => props.theme.colors.background.primary};
  border-radius: ${props => props.theme.spacing.radius.circle};
`;

const getInitials = (name: string): string => {
  const words = name.trim().split(' ');
  if (words.length === 1) {
    return words[0].substring(0, 2).toUpperCase();
  }
  return (words[0][0] + words[words.length - 1][0]).toUpperCase();
};

export const Avatar: React.FC<AvatarProps> = ({
  src,
  alt,
  name,
  size = 'medium',
  shape = 'circle',
  status,
}) => {
  const displayName = name || alt || 'User';
  const initials = getInitials(displayName);

  return (
    <AvatarContainer size={size}>
      <StyledAvatar shape={shape} hasImage={!!src}>
        {src ? (
          <AvatarImage src={src} alt={alt || name} />
        ) : (
          <span>{initials}</span>
        )}
      </StyledAvatar>
      {status && <StatusIndicator status={status} size={size} />}
    </AvatarContainer>
  );
};