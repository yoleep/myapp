import React, { useState } from 'react';
import styled from 'styled-components';
import { Theme } from '../../styles/theme';
import { Card } from '../Card';
import { Badge } from '../Badge';

export interface ThemeShowcaseProps {
  theme: Theme;
}

const ShowcaseContainer = styled.div`
  display: grid;
  gap: 32px;
`;

const SectionTitle = styled.h3`
  font-size: ${props => props.theme.typography.fontSize.large};
  color: ${props => props.theme.colors.text.primary};
  margin-bottom: 16px;
`;

const ColorGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 16px;
`;

const ColorCard = styled.div<{ theme: Theme }>`
  background: ${props => props.theme.colors.background.secondary};
  border: 1px solid ${props => props.theme.colors.border.primary};
  border-radius: ${props => props.theme.spacing.radius['8']};
  overflow: hidden;
  transition: transform ${props => props.theme.effects.transitions.quick} ease;
  cursor: pointer;
  
  &:hover {
    transform: translateY(-2px);
    border-color: ${props => props.theme.colors.border.secondary};
  }
`;

const ColorSwatch = styled.div<{ color: string }>`
  height: 80px;
  background: ${props => props.color};
  position: relative;
`;

const ColorInfo = styled.div`
  padding: 12px;
`;

const ColorName = styled.div`
  font-size: ${props => props.theme.typography.fontSize.small};
  font-weight: ${props => props.theme.typography.fontWeight.medium};
  color: ${props => props.theme.colors.text.primary};
  margin-bottom: 4px;
`;

const ColorValue = styled.div`
  font-size: ${props => props.theme.typography.fontSize.mini};
  color: ${props => props.theme.colors.text.tertiary};
  font-family: ${props => props.theme.typography.fontFamily.monospace};
  word-break: break-all;
`;

const TypographyGrid = styled.div`
  display: flex;
  flex-direction: column;
  gap: 24px;
`;

const TypographyItem = styled.div`
  display: flex;
  align-items: baseline;
  gap: 16px;
  padding: 16px;
  background: ${props => props.theme.colors.background.secondary};
  border: 1px solid ${props => props.theme.colors.border.primary};
  border-radius: ${props => props.theme.spacing.radius['8']};
`;

const TypographyLabel = styled.span`
  font-size: ${props => props.theme.typography.fontSize.mini};
  color: ${props => props.theme.colors.text.tertiary};
  min-width: 120px;
  font-family: ${props => props.theme.typography.fontFamily.monospace};
`;

const TypographyExample = styled.span<{ 
  fontSize?: string;
  fontWeight?: string;
  fontFamily?: string;
}>`
  font-size: ${props => props.fontSize || 'inherit'};
  font-weight: ${props => props.fontWeight || 'inherit'};
  font-family: ${props => props.fontFamily || 'inherit'};
  color: ${props => props.theme.colors.text.primary};
`;

const SpacingGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 16px;
`;

const SpacingItem = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px;
  background: ${props => props.theme.colors.background.secondary};
  border: 1px solid ${props => props.theme.colors.border.primary};
  border-radius: ${props => props.theme.spacing.radius['8']};
`;

const SpacingBox = styled.div<{ size: string }>`
  width: ${props => props.size};
  height: ${props => props.size};
  background: ${props => props.theme.colors.brand.primary};
  border-radius: ${props => props.theme.spacing.radius['4']};
`;

const SpacingLabel = styled.span`
  font-size: ${props => props.theme.typography.fontSize.mini};
  color: ${props => props.theme.colors.text.tertiary};
  font-family: ${props => props.theme.typography.fontFamily.monospace};
`;

const TabContainer = styled.div`
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  border-bottom: 1px solid ${props => props.theme.colors.border.primary};
`;

const Tab = styled.button<{ isActive: boolean; theme: Theme }>`
  padding: 12px 16px;
  background: transparent;
  border: none;
  color: ${props => props.isActive 
    ? props.theme.colors.text.primary 
    : props.theme.colors.text.tertiary};
  font-size: ${props => props.theme.typography.fontSize.regular};
  font-weight: ${props => props.theme.typography.fontWeight.medium};
  cursor: pointer;
  position: relative;
  transition: color ${props => props.theme.effects.transitions.quick} ease;
  
  &:hover {
    color: ${props => props.theme.colors.text.primary};
  }
  
  ${props => props.isActive && `
    &::after {
      content: '';
      position: absolute;
      bottom: -1px;
      left: 0;
      right: 0;
      height: 2px;
      background: ${props.theme.colors.brand.primary};
    }
  `}
`;

const CopyButton = styled.button<{ theme: Theme }>`
  position: absolute;
  top: 8px;
  right: 8px;
  padding: 4px 8px;
  background: ${props => props.theme.colors.background.primary}cc;
  border: 1px solid ${props => props.theme.colors.border.secondary};
  border-radius: ${props => props.theme.spacing.radius['4']};
  color: ${props => props.theme.colors.text.secondary};
  font-size: ${props => props.theme.typography.fontSize.mini};
  cursor: pointer;
  opacity: 0;
  transition: opacity ${props => props.theme.effects.transitions.quick} ease;
  
  ${ColorCard}:hover & {
    opacity: 1;
  }
  
  &:hover {
    background: ${props => props.theme.colors.background.secondary};
    color: ${props => props.theme.colors.text.primary};
  }
`;

const EffectsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
`;

const EffectCard = styled.div<{ theme: Theme }>`
  padding: 24px;
  background: ${props => props.theme.colors.background.secondary};
  border: 1px solid ${props => props.theme.colors.border.primary};
  border-radius: ${props => props.theme.spacing.radius['12']};
  text-align: center;
`;

const ShadowBox = styled.div<{ shadow: string; theme: Theme }>`
  width: 80px;
  height: 80px;
  margin: 0 auto 16px;
  background: ${props => props.theme.colors.background.tertiary};
  border-radius: ${props => props.theme.spacing.radius['8']};
  box-shadow: ${props => props.shadow};
`;

export const ThemeShowcase: React.FC<ThemeShowcaseProps> = ({ theme }) => {
  const [activeTab, setActiveTab] = useState<'colors' | 'typography' | 'spacing' | 'effects'>('colors');
  const [copiedValue, setCopiedValue] = useState<string | null>(null);

  const copyToClipboard = (value: string) => {
    navigator.clipboard.writeText(value);
    setCopiedValue(value);
    setTimeout(() => setCopiedValue(null), 2000);
  };

  const renderColorSection = (title: string, colors: Record<string, string>) => (
    <>
      <SectionTitle>{title}</SectionTitle>
      <ColorGrid>
        {Object.entries(colors).map(([name, value]) => (
          <ColorCard key={name} onClick={() => copyToClipboard(value)}>
            <ColorSwatch color={value}>
              <CopyButton>
                {copiedValue === value ? 'Copied!' : 'Copy'}
              </CopyButton>
            </ColorSwatch>
            <ColorInfo>
              <ColorName>{name}</ColorName>
              <ColorValue>{value}</ColorValue>
            </ColorInfo>
          </ColorCard>
        ))}
      </ColorGrid>
    </>
  );

  return (
    <ShowcaseContainer>
      <TabContainer>
        <Tab isActive={activeTab === 'colors'} onClick={() => setActiveTab('colors')}>
          Colors
        </Tab>
        <Tab isActive={activeTab === 'typography'} onClick={() => setActiveTab('typography')}>
          Typography
        </Tab>
        <Tab isActive={activeTab === 'spacing'} onClick={() => setActiveTab('spacing')}>
          Spacing
        </Tab>
        <Tab isActive={activeTab === 'effects'} onClick={() => setActiveTab('effects')}>
          Effects
        </Tab>
      </TabContainer>

      {activeTab === 'colors' && (
        <>
          {renderColorSection('Brand Colors', theme.colors.brand)}
          {renderColorSection('Background Colors', theme.colors.background)}
          {renderColorSection('Text Colors', theme.colors.text)}
          {renderColorSection('Border Colors', theme.colors.border)}
          {renderColorSection('Status Colors', theme.colors.status)}
        </>
      )}

      {activeTab === 'typography' && (
        <>
          <SectionTitle>Font Sizes</SectionTitle>
          <TypographyGrid>
            {Object.entries(theme.typography.fontSize).map(([name, value]) => (
              <TypographyItem key={name}>
                <TypographyLabel>{name}: {value}</TypographyLabel>
                <TypographyExample fontSize={value}>
                  The quick brown fox jumps over the lazy dog
                </TypographyExample>
              </TypographyItem>
            ))}
          </TypographyGrid>

          <SectionTitle style={{ marginTop: '32px' }}>Font Weights</SectionTitle>
          <TypographyGrid>
            {Object.entries(theme.typography.fontWeight).map(([name, value]) => (
              <TypographyItem key={name}>
                <TypographyLabel>{name}: {value}</TypographyLabel>
                <TypographyExample fontWeight={value}>
                  The quick brown fox jumps over the lazy dog
                </TypographyExample>
              </TypographyItem>
            ))}
          </TypographyGrid>

          <SectionTitle style={{ marginTop: '32px' }}>Headings</SectionTitle>
          <TypographyGrid>
            {Object.entries(theme.typography.headings).map(([name, styles]) => (
              <TypographyItem key={name}>
                <TypographyLabel>{name.toUpperCase()}</TypographyLabel>
                <TypographyExample 
                  fontSize={styles.fontSize}
                  fontWeight={styles.fontWeight}
                  style={{ 
                    lineHeight: styles.lineHeight,
                    letterSpacing: styles.letterSpacing 
                  }}
                >
                  Heading Example
                </TypographyExample>
              </TypographyItem>
            ))}
          </TypographyGrid>
        </>
      )}

      {activeTab === 'spacing' && (
        <>
          <SectionTitle>Border Radius</SectionTitle>
          <SpacingGrid>
            {Object.entries(theme.spacing.radius).map(([name, value]) => (
              <SpacingItem key={name}>
                <div 
                  style={{ 
                    width: '60px', 
                    height: '60px', 
                    background: theme.colors.brand.primary,
                    borderRadius: value 
                  }} 
                />
                <SpacingLabel>{name}</SpacingLabel>
                <SpacingLabel>{value}</SpacingLabel>
              </SpacingItem>
            ))}
          </SpacingGrid>

          <SectionTitle style={{ marginTop: '32px' }}>Layout Spacing</SectionTitle>
          <TypographyGrid>
            {Object.entries(theme.spacing.layout).map(([name, value]) => (
              <Card key={name} padding="small">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span style={{ fontWeight: 500 }}>{name}</span>
                  <Badge variant="primary">{value}</Badge>
                </div>
              </Card>
            ))}
          </TypographyGrid>
        </>
      )}

      {activeTab === 'effects' && (
        <>
          <SectionTitle>Shadows</SectionTitle>
          <EffectsGrid>
            {Object.entries(theme.effects.shadows).map(([name, value]) => (
              <EffectCard key={name}>
                <ShadowBox shadow={value} />
                <ColorName>{name}</ColorName>
              </EffectCard>
            ))}
          </EffectsGrid>

          <SectionTitle style={{ marginTop: '32px' }}>Transitions</SectionTitle>
          <TypographyGrid>
            {Object.entries(theme.effects.transitions).map(([name, value]) => (
              <Card key={name} padding="small">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span style={{ fontWeight: 500 }}>{name}</span>
                  <Badge variant="info">{value}</Badge>
                </div>
              </Card>
            ))}
          </TypographyGrid>

          <SectionTitle style={{ marginTop: '32px' }}>Easing Functions</SectionTitle>
          <TypographyGrid>
            {Object.entries(theme.effects.easing).map(([name, value]) => (
              <Card key={name} padding="small">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span style={{ fontWeight: 500 }}>{name}</span>
                  <code style={{ 
                    fontSize: theme.typography.fontSize.mini,
                    color: theme.colors.text.tertiary,
                    fontFamily: theme.typography.fontFamily.monospace
                  }}>
                    {value}
                  </code>
                </div>
              </Card>
            ))}
          </TypographyGrid>
        </>
      )}
    </ShowcaseContainer>
  );
};