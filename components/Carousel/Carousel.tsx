import React, { useState, useEffect, useRef } from 'react';
import styled, { css } from 'styled-components';
import { Theme } from '../../styles/theme';

export interface CarouselItem {
  id: string | number;
  content: React.ReactNode;
}

export interface CarouselProps {
  items: CarouselItem[];
  autoPlay?: boolean;
  autoPlayInterval?: number;
  showIndicators?: boolean;
  showArrows?: boolean;
  height?: string;
  infiniteLoop?: boolean;
  onSlideChange?: (index: number) => void;
}

const CarouselContainer = styled.div<{ height?: string }>`
  position: relative;
  width: 100%;
  height: ${props => props.height || '400px'};
  overflow: hidden;
  border-radius: ${props => props.theme.spacing.radius['12']};
  background: ${props => props.theme.colors.background.secondary};
`;

const CarouselTrack = styled.div<{ 
  currentIndex: number;
  itemCount: number;
  theme: Theme;
}>`
  display: flex;
  height: 100%;
  transition: transform ${props => props.theme.effects.transitions.regular} ${props => props.theme.effects.easing.inOutQuad};
  transform: translateX(-${props => props.currentIndex * 100}%);
`;

const CarouselSlide = styled.div`
  flex: 0 0 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
`;

const ArrowButton = styled.button<{ 
  position: 'left' | 'right';
  theme: Theme;
}>`
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  ${props => props.position === 'left' ? 'left: 16px' : 'right: 16px'};
  z-index: 2;
  width: 40px;
  height: 40px;
  border-radius: ${props => props.theme.spacing.radius.circle};
  background: ${props => props.theme.colors.background.primary}cc;
  backdrop-filter: blur(10px);
  border: 1px solid ${props => props.theme.colors.border.secondary};
  color: ${props => props.theme.colors.text.primary};
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all ${props => props.theme.effects.transitions.quick} ease;
  
  &:hover {
    background: ${props => props.theme.colors.background.secondary};
    border-color: ${props => props.theme.colors.border.tertiary};
    transform: translateY(-50%) scale(1.1);
  }
  
  &:active {
    transform: translateY(-50%) scale(0.95);
  }
  
  &:disabled {
    opacity: 0.3;
    cursor: not-allowed;
    
    &:hover {
      transform: translateY(-50%);
    }
  }
`;

const ArrowIcon = styled.svg`
  width: 20px;
  height: 20px;
`;

const IndicatorContainer = styled.div`
  position: absolute;
  bottom: 16px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 8px;
  z-index: 2;
  padding: 8px 12px;
  background: ${props => props.theme.colors.background.primary}cc;
  backdrop-filter: blur(10px);
  border-radius: ${props => props.theme.spacing.radius.rounded};
  border: 1px solid ${props => props.theme.colors.border.secondary};
`;

const Indicator = styled.button<{ 
  active: boolean;
  theme: Theme;
}>`
  width: ${props => props.active ? '24px' : '8px'};
  height: 8px;
  border-radius: ${props => props.theme.spacing.radius.rounded};
  border: none;
  background: ${props => props.active 
    ? props.theme.colors.brand.primary 
    : props.theme.colors.border.tertiary};
  cursor: pointer;
  transition: all ${props => props.theme.effects.transitions.regular} ease;
  
  &:hover {
    background: ${props => props.active 
      ? props.theme.colors.brand.accent 
      : props.theme.colors.text.quaternary};
  }
`;

const PlayPauseButton = styled.button<{ theme: Theme }>`
  position: absolute;
  top: 16px;
  right: 16px;
  z-index: 2;
  width: 32px;
  height: 32px;
  border-radius: ${props => props.theme.spacing.radius.circle};
  background: ${props => props.theme.colors.background.primary}cc;
  backdrop-filter: blur(10px);
  border: 1px solid ${props => props.theme.colors.border.secondary};
  color: ${props => props.theme.colors.text.secondary};
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all ${props => props.theme.effects.transitions.quick} ease;
  
  &:hover {
    background: ${props => props.theme.colors.background.secondary};
    border-color: ${props => props.theme.colors.border.tertiary};
    color: ${props => props.theme.colors.text.primary};
  }
`;

export const Carousel: React.FC<CarouselProps> = ({
  items = [],
  autoPlay = false,
  autoPlayInterval = 3000,
  showIndicators = true,
  showArrows = true,
  height,
  infiniteLoop = false,
  onSlideChange,
}) => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [isPlaying, setIsPlaying] = useState(autoPlay);
  const intervalRef = useRef<NodeJS.Timeout | null>(null);
  
  // Return empty if no items
  if (!items || items.length === 0) {
    return (
      <CarouselContainer height={height}>
        <CarouselSlide>
          <div style={{ color: '#8a8f98' }}>No items to display</div>
        </CarouselSlide>
      </CarouselContainer>
    );
  }

  const handlePrevious = () => {
    const newIndex = infiniteLoop 
      ? (currentIndex - 1 + items.length) % items.length
      : Math.max(0, currentIndex - 1);
    setCurrentIndex(newIndex);
    onSlideChange?.(newIndex);
  };

  const handleNext = () => {
    const newIndex = infiniteLoop 
      ? (currentIndex + 1) % items.length
      : Math.min(items.length - 1, currentIndex + 1);
    setCurrentIndex(newIndex);
    onSlideChange?.(newIndex);
  };

  useEffect(() => {
    if (isPlaying && items && items.length > 1) {
      intervalRef.current = setInterval(() => {
        setCurrentIndex(prevIndex => {
          const newIndex = infiniteLoop 
            ? (prevIndex + 1) % items.length
            : Math.min(items.length - 1, prevIndex + 1);
          onSlideChange?.(newIndex);
          return newIndex;
        });
      }, autoPlayInterval);
    }
    
    return () => {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
      }
    };
  }, [isPlaying, items, autoPlayInterval, infiniteLoop, onSlideChange]);

  const handleIndicatorClick = (index: number) => {
    setCurrentIndex(index);
    onSlideChange?.(index);
  };

  const togglePlayPause = () => {
    setIsPlaying(!isPlaying);
  };

  const canGoPrevious = infiniteLoop || currentIndex > 0;
  const canGoNext = infiniteLoop || currentIndex < items.length - 1;

  return (
    <CarouselContainer height={height}>
      {showArrows && items.length > 1 && (
        <>
          <ArrowButton 
            position="left" 
            onClick={handlePrevious}
            disabled={!canGoPrevious}
            aria-label="Previous slide"
          >
            <ArrowIcon viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
            </ArrowIcon>
          </ArrowButton>
          <ArrowButton 
            position="right" 
            onClick={handleNext}
            disabled={!canGoNext}
            aria-label="Next slide"
          >
            <ArrowIcon viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
            </ArrowIcon>
          </ArrowButton>
        </>
      )}
      
      {autoPlay && items.length > 1 && (
        <PlayPauseButton onClick={togglePlayPause} aria-label={isPlaying ? 'Pause' : 'Play'}>
          {isPlaying ? (
            <ArrowIcon viewBox="0 0 24 24" fill="currentColor">
              <rect x="6" y="4" width="4" height="16" />
              <rect x="14" y="4" width="4" height="16" />
            </ArrowIcon>
          ) : (
            <ArrowIcon viewBox="0 0 24 24" fill="currentColor">
              <path d="M8 5v14l11-7z" />
            </ArrowIcon>
          )}
        </PlayPauseButton>
      )}
      
      <CarouselTrack currentIndex={currentIndex} itemCount={items.length}>
        {items.map((item) => (
          <CarouselSlide key={item.id}>
            {item.content}
          </CarouselSlide>
        ))}
      </CarouselTrack>
      
      {showIndicators && items.length > 1 && (
        <IndicatorContainer>
          {items.map((_, index) => (
            <Indicator
              key={index}
              active={index === currentIndex}
              onClick={() => handleIndicatorClick(index)}
              aria-label={`Go to slide ${index + 1}`}
            />
          ))}
        </IndicatorContainer>
      )}
    </CarouselContainer>
  );
};