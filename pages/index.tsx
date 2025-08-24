import React, { useState } from 'react';
import Link from 'next/link';
import styled from 'styled-components';
import { 
  Button, 
  Card, 
  Layout, 
  Navbar, 
  Footer, 
  Hero, 
  Badge, 
  Input,
  List,
  Carousel
} from '../components';

const ProductGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  margin: 48px 0;
`;

const CategorySection = styled.section`
  margin: 64px 0;
`;

const SectionTitle = styled.h2`
  font-size: ${props => props.theme.typography.fontSize.h2};
  margin-bottom: 32px;
  color: ${props => props.theme.colors.text.primary};
`;

const PromoSection = styled.div`
  margin: 48px 0;
  padding: 48px;
  background: ${props => props.theme.colors.background.secondary};
  border-radius: ${props => props.theme.spacing.radius[16]}px;
  text-align: center;
`;

const FeatureGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  margin: 48px 0;
`;

export default function HomePage() {
  const [searchQuery, setSearchQuery] = useState('');

  // 네비게이션 메뉴
  const navLinks = [
    { href: '#', label: '노트북' },
    { href: '#', label: '스마트폰' },
    { href: '#', label: '태블릿' },
    { href: '#', label: '액세서리' },
    { href: '#', label: '할인상품' },
  ];

  // 베스트셀러 상품 데이터
  const bestProducts = [
    {
      id: 1,
      name: 'MacBook Pro 14"',
      price: '2,290,000',
      originalPrice: '2,590,000',
      discount: '-12%',
      rating: 4.8,
      image: 'https://via.placeholder.com/280x200/1c1c1f/8a8f98?text=MacBook+Pro',
      badge: 'HOT'
    },
    {
      id: 2,
      name: 'iPhone 15 Pro Max',
      price: '1,590,000',
      originalPrice: '1,730,000',
      discount: '-8%',
      rating: 4.9,
      image: 'https://via.placeholder.com/280x200/1c1c1f/8a8f98?text=MacBook+Pro',
      badge: 'BEST'
    },
    {
      id: 3,
      name: 'Galaxy Tab S9 Ultra',
      price: '1,290,000',
      originalPrice: '1,490,000',
      discount: '-13%',
      rating: 4.7,
      image: 'https://via.placeholder.com/280x200/1c1c1f/8a8f98?text=MacBook+Pro',
      badge: 'NEW'
    },
    {
      id: 4,
      name: 'AirPods Pro 2',
      price: '329,000',
      originalPrice: '359,000',
      discount: '-8%',
      rating: 4.8,
      image: 'https://via.placeholder.com/280x200/1c1c1f/8a8f98?text=MacBook+Pro',
      badge: null
    },
  ];

  // 신제품 데이터
  const newProducts = [
    {
      id: 5,
      name: 'Dell XPS 13 Plus',
      price: '1,890,000',
      originalPrice: '2,090,000',
      discount: '-10%',
      rating: 4.6,
      image: 'https://via.placeholder.com/280x200/1c1c1f/8a8f98?text=MacBook+Pro',
      badge: 'NEW'
    },
    {
      id: 6,
      name: 'Sony WH-1000XM5',
      price: '399,000',
      originalPrice: '499,000',
      discount: '-20%',
      rating: 4.9,
      image: 'https://via.placeholder.com/280x200/1c1c1f/8a8f98?text=MacBook+Pro',
      badge: 'SALE'
    },
    {
      id: 7,
      name: 'iPad Air 5',
      price: '890,000',
      originalPrice: '990,000',
      discount: '-10%',
      rating: 4.7,
      image: 'https://via.placeholder.com/280x200/1c1c1f/8a8f98?text=MacBook+Pro',
      badge: null
    },
    {
      id: 8,
      name: 'Samsung Galaxy Watch 6',
      price: '329,000',
      originalPrice: '399,000',
      discount: '-18%',
      rating: 4.5,
      image: 'https://via.placeholder.com/280x200/1c1c1f/8a8f98?text=MacBook+Pro',
      badge: 'NEW'
    },
  ];

  // 캐러셀 이미지 데이터
  const carouselImages = [
    {
      src: 'https://via.placeholder.com/1200x400/5e6ad2/ffffff?text=Black+Friday',
      alt: '블랙프라이데이 최대 50% 할인',
      title: '블랙프라이데이 특가',
      subtitle: '최대 50% 할인',
      action: '지금 쇼핑하기'
    },
    {
      src: 'https://via.placeholder.com/1200x400/5e6ad2/ffffff?text=Black+Friday',
      alt: '신제품 iPhone 15 시리즈',
      title: 'iPhone 15 시리즈',
      subtitle: '지금 사전예약 하세요',
      action: '자세히 보기'
    },
    {
      src: 'https://via.placeholder.com/1200x400/5e6ad2/ffffff?text=Black+Friday',
      alt: '갤럭시 신제품 출시',
      title: 'Galaxy 신제품',
      subtitle: '혁신적인 기술의 완성',
      action: '구매하기'
    },
  ];

  // Footer 섹션 데이터
  const footerSections = [
    {
      title: '고객 서비스',
      links: [
        { href: '#', label: '문의하기' },
        { href: '#', label: '배송 정보' },
        { href: '#', label: '반품/교환' },
        { href: '#', label: 'FAQ' },
      ],
    },
    {
      title: '회사 정보',
      links: [
        { href: '#', label: '회사 소개' },
        { href: '#', label: '채용 정보' },
        { href: '#', label: '이용약관' },
        { href: '#', label: '개인정보처리방침' },
      ],
    },
    {
      title: '쇼핑 안내',
      links: [
        { href: '#', label: '할인/이벤트' },
        { href: '#', label: '멤버십 혜택' },
        { href: '#', label: '기프트 카드' },
        { href: '#', label: '제휴 카드' },
      ],
    },
  ];

  return (
    <Layout
      navbar={{
        title: 'TechStore',
        links: navLinks,
        rightContent: (
          <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
            <Input
              placeholder="검색..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              style={{ width: '200px' }}
            />
            <Button variant="ghost">로그인</Button>
            <Button variant="primary">장바구니</Button>
          </div>
        ),
      }}
      footer={{
        sections: footerSections,
        bottomText: '© 2024 TechStore. All rights reserved.',
      }}
    >
      {/* 히어로 섹션 - 캐러셀 */}
      <Carousel
        items={carouselImages.map((img, index) => ({
          id: index,
          content: (
            <div style={{ 
              position: 'relative',
              width: '100%',
              height: '100%',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              background: `linear-gradient(rgba(0,0,0,0.4), rgba(0,0,0,0.4)), url(${img.src})`,
              backgroundSize: 'cover',
              backgroundPosition: 'center'
            }}>
              <div style={{ 
                textAlign: 'center',
                color: 'white',
                zIndex: 1
              }}>
                <h1 style={{ fontSize: '48px', marginBottom: '16px' }}>{img.title}</h1>
                <p style={{ fontSize: '24px', marginBottom: '24px' }}>{img.subtitle}</p>
                <Button variant="primary" size="large">{img.action}</Button>
              </div>
            </div>
          )
        }))}
        autoPlay
        autoPlayInterval={5000}
        height="500px"
        infiniteLoop
        showIndicators
        showArrows
      />

      {/* 프로모션 섹션 */}
      <PromoSection>
        <h2 style={{ fontSize: '32px', marginBottom: '16px' }}>🎉 연말 특별 할인</h2>
        <p style={{ fontSize: '18px', marginBottom: '24px', color: '#8a8f98' }}>
          모든 전자제품 최대 30% 할인 + 무료배송
        </p>
        <Button variant="primary" size="large">할인 상품 보기</Button>
      </PromoSection>

      {/* 베스트셀러 섹션 */}
      <CategorySection>
        <SectionTitle>🔥 베스트셀러</SectionTitle>
        <ProductGrid>
          {bestProducts.map((product) => (
            <Card key={product.id} variant="elevated">
              <div style={{ position: 'relative' }}>
                <img 
                  src={`https://via.placeholder.com/280x200/1c1c1f/8a8f98?text=${encodeURIComponent(product.name.split(' ')[0])}`} 
                  alt={product.name}
                  style={{ 
                    width: '100%', 
                    height: '200px', 
                    objectFit: 'cover',
                    borderRadius: '8px',
                    marginBottom: '16px'
                  }}
                />
                {product.badge && (
                  <div style={{ position: 'absolute', top: '8px', right: '8px' }}>
                    <Badge 
                      variant={product.badge === 'HOT' ? 'danger' : 
                               product.badge === 'NEW' ? 'success' : 'warning'}
                    >
                      {product.badge}
                    </Badge>
                  </div>
                )}
              </div>
              <h3 style={{ marginBottom: '8px' }}>{product.name}</h3>
              <div style={{ marginBottom: '8px' }}>
                <span style={{ color: '#8a8f98', textDecoration: 'line-through', marginRight: '8px' }}>
                  ₩{product.originalPrice}
                </span>
                <Badge variant="danger">{product.discount}</Badge>
              </div>
              <p style={{ fontSize: '20px', fontWeight: 'bold', marginBottom: '12px' }}>
                ₩{product.price}
              </p>
              <div style={{ display: 'flex', alignItems: 'center', marginBottom: '16px' }}>
                <span style={{ color: '#ffd60a', marginRight: '4px' }}>★</span>
                <span>{product.rating}</span>
              </div>
              <Button variant="primary" fullWidth>장바구니 담기</Button>
            </Card>
          ))}
        </ProductGrid>
      </CategorySection>

      {/* 신제품 섹션 */}
      <CategorySection>
        <SectionTitle>✨ 신제품</SectionTitle>
        <ProductGrid>
          {newProducts.map((product) => (
            <Card key={product.id} variant="elevated">
              <div style={{ position: 'relative' }}>
                <img 
                  src={`https://via.placeholder.com/280x200/1c1c1f/8a8f98?text=${encodeURIComponent(product.name.split(' ')[0])}`} 
                  alt={product.name}
                  style={{ 
                    width: '100%', 
                    height: '200px', 
                    objectFit: 'cover',
                    borderRadius: '8px',
                    marginBottom: '16px'
                  }}
                />
                {product.badge && (
                  <div style={{ position: 'absolute', top: '8px', right: '8px' }}>
                    <Badge 
                      variant={product.badge === 'SALE' ? 'danger' : 
                               product.badge === 'NEW' ? 'success' : 'warning'}
                    >
                      {product.badge}
                    </Badge>
                  </div>
                )}
              </div>
              <h3 style={{ marginBottom: '8px' }}>{product.name}</h3>
              <div style={{ marginBottom: '8px' }}>
                <span style={{ color: '#8a8f98', textDecoration: 'line-through', marginRight: '8px' }}>
                  ₩{product.originalPrice}
                </span>
                <Badge variant="danger">{product.discount}</Badge>
              </div>
              <p style={{ fontSize: '20px', fontWeight: 'bold', marginBottom: '12px' }}>
                ₩{product.price}
              </p>
              <div style={{ display: 'flex', alignItems: 'center', marginBottom: '16px' }}>
                <span style={{ color: '#ffd60a', marginRight: '4px' }}>★</span>
                <span>{product.rating}</span>
              </div>
              <Button variant="primary" fullWidth>장바구니 담기</Button>
            </Card>
          ))}
        </ProductGrid>
      </CategorySection>

      {/* 특징 섹션 */}
      <CategorySection>
        <SectionTitle>왜 TechStore인가요?</SectionTitle>
        <FeatureGrid>
          <Card variant="elevated" padding="large">
            <h3 style={{ marginBottom: '16px' }}>🚚 무료 배송</h3>
            <p style={{ color: '#8a8f98' }}>5만원 이상 구매시 전국 무료배송</p>
          </Card>
          <Card variant="elevated" padding="large">
            <h3 style={{ marginBottom: '16px' }}>💳 안전한 결제</h3>
            <p style={{ color: '#8a8f98' }}>다양한 결제 수단과 보안 시스템</p>
          </Card>
          <Card variant="elevated" padding="large">
            <h3 style={{ marginBottom: '16px' }}>🔄 30일 반품</h3>
            <p style={{ color: '#8a8f98' }}>구매 후 30일 이내 무료 반품 가능</p>
          </Card>
          <Card variant="elevated" padding="large">
            <h3 style={{ marginBottom: '16px' }}>📞 24/7 고객지원</h3>
            <p style={{ color: '#8a8f98' }}>언제든지 도움을 받으실 수 있습니다</p>
          </Card>
        </FeatureGrid>
      </CategorySection>

      {/* 뉴스레터 섹션 */}
      <PromoSection>
        <h2 style={{ fontSize: '28px', marginBottom: '16px' }}>📧 뉴스레터 구독</h2>
        <p style={{ fontSize: '16px', marginBottom: '24px', color: '#8a8f98' }}>
          최신 제품과 특별 할인 정보를 받아보세요
        </p>
        <div style={{ display: 'flex', gap: '16px', justifyContent: 'center', maxWidth: '400px', margin: '0 auto' }}>
          <Input 
            placeholder="이메일 주소" 
            type="email"
            style={{ flex: 1 }}
          />
          <Button variant="primary">구독하기</Button>
        </div>
      </PromoSection>
    </Layout>
  );
}