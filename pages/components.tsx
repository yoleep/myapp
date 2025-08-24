import React, { useState, useMemo } from 'react';
import styled from 'styled-components';
import { Button, Input, Card, Badge, Avatar, Select, Carousel, Navbar, Footer, Hero, Layout, ThemeShowcase, List, Pagination } from '../components';
import type { SelectOption, CarouselItem, NavLink, FooterSection, SocialLink, ListColumn } from '../components';
import { theme } from '../styles/theme';

const PageContainer = styled.div`
  min-height: 100vh;
  padding: 40px 24px;
  max-width: 1200px;
  margin: 0 auto;
`;

const PageTitle = styled.h1`
  margin-bottom: 48px;
`;

const Section = styled.section`
  margin-bottom: 64px;
`;

const SectionTitle = styled.h2`
  margin-bottom: 24px;
  padding-bottom: 12px;
  border-bottom: 1px solid ${props => props.theme.colors.border.primary};
`;

const ComponentGrid = styled.div`
  display: grid;
  gap: 24px;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
`;

const ComponentBox = styled.div`
  display: flex;
  flex-direction: column;
  gap: 16px;
`;

const ComponentLabel = styled.h3`
  font-size: ${props => props.theme.typography.fontSize.regular};
  color: ${props => props.theme.colors.text.secondary};
  margin-bottom: 8px;
`;

const FlexRow = styled.div`
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
`;

const SpacedRow = styled.div`
  display: flex;
  gap: 24px;
  align-items: flex-start;
  flex-wrap: wrap;
`;

export default function ComponentsPage() {
  const [inputValue, setInputValue] = useState('');
  const [selectValue, setSelectValue] = useState<string | number>('');
  
  const selectOptions: SelectOption[] = [
    { value: 'option1', label: 'Option 1' },
    { value: 'option2', label: 'Option 2' },
    { value: 'option3', label: 'Option 3', disabled: true },
    { value: 'option4', label: 'Option 4' },
  ];

  return (
    <PageContainer>
      <PageTitle>Linear Design System Components</PageTitle>

      <Section>
        <SectionTitle>Buttons</SectionTitle>
        <ComponentGrid>
          <ComponentBox>
            <ComponentLabel>Button Variants</ComponentLabel>
            <FlexRow>
              <Button variant="primary">Primary</Button>
              <Button variant="secondary">Secondary</Button>
              <Button variant="ghost">Ghost</Button>
              <Button variant="danger">Danger</Button>
            </FlexRow>
          </ComponentBox>
          
          <ComponentBox>
            <ComponentLabel>Button Sizes</ComponentLabel>
            <FlexRow>
              <Button size="small">Small</Button>
              <Button size="medium">Medium</Button>
              <Button size="large">Large</Button>
            </FlexRow>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Button States</ComponentLabel>
            <FlexRow>
              <Button disabled>Disabled</Button>
              <Button isLoading>Loading</Button>
              <Button fullWidth>Full Width</Button>
            </FlexRow>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Buttons with Icons</ComponentLabel>
            <FlexRow>
              <Button leftIcon={<span>👈</span>}>Left Icon</Button>
              <Button rightIcon={<span>👉</span>}>Right Icon</Button>
              <Button leftIcon={<span>⚡</span>} rightIcon={<span>⚡</span>}>Both Icons</Button>
            </FlexRow>
          </ComponentBox>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>Inputs</SectionTitle>
        <ComponentGrid>
          <ComponentBox>
            <ComponentLabel>Basic Input</ComponentLabel>
            <Input 
              placeholder="Enter text..." 
              value={inputValue}
              onChange={(e) => setInputValue(e.target.value)}
            />
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Input with Label</ComponentLabel>
            <Input 
              label="Email Address"
              placeholder="user@example.com"
              type="email"
            />
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Input with Helper Text</ComponentLabel>
            <Input 
              label="Password"
              type="password"
              placeholder="Enter password"
              helperText="Must be at least 8 characters"
            />
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Input with Error</ComponentLabel>
            <Input 
              label="Username"
              placeholder="Enter username"
              error="This username is already taken"
            />
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Input with Icons</ComponentLabel>
            <Input 
              leftIcon={<span>🔍</span>}
              placeholder="Search..."
            />
            <Input 
              rightIcon={<span>👁</span>}
              placeholder="Password"
              type="password"
            />
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Full Width Input</ComponentLabel>
            <Input 
              fullWidth
              placeholder="Full width input"
              label="Full Width"
            />
          </ComponentBox>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>Select</SectionTitle>
        <ComponentGrid>
          <ComponentBox>
            <ComponentLabel>Basic Select</ComponentLabel>
            <Select 
              options={selectOptions}
              value={selectValue}
              onChange={setSelectValue}
              placeholder="Choose an option"
            />
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Select with Label</ComponentLabel>
            <Select 
              label="Choose Option"
              options={selectOptions}
              placeholder="Select one"
            />
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Select with Helper Text</ComponentLabel>
            <Select 
              label="Category"
              options={selectOptions}
              helperText="Select a category for your item"
            />
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Select with Error</ComponentLabel>
            <Select 
              label="Required Field"
              options={selectOptions}
              error="This field is required"
            />
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Disabled Select</ComponentLabel>
            <Select 
              options={selectOptions}
              disabled
              placeholder="Disabled select"
            />
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Full Width Select</ComponentLabel>
            <Select 
              options={selectOptions}
              fullWidth
              label="Full Width Select"
            />
          </ComponentBox>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>Cards</SectionTitle>
        <ComponentGrid>
          <Card>
            <h3>Default Card</h3>
            <p>This is a default card with medium padding.</p>
          </Card>

          <Card variant="elevated">
            <h3>Elevated Card</h3>
            <p>This card has a subtle shadow effect.</p>
          </Card>

          <Card variant="outlined">
            <h3>Outlined Card</h3>
            <p>This card has a transparent background with border.</p>
          </Card>

          <Card
            image={{
              src: 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800',
              alt: 'Code on screen',
              position: 'top'
            }}
            title="Card with Top Image"
            subtitle="Beautiful code editor"
          >
            <p>This card has an image at the top with title and subtitle.</p>
          </Card>

          <Card
            image={{
              src: 'https://images.unsplash.com/photo-1517180102446-f3ece451e9d8?w=800',
              alt: 'Setup workspace',
              position: 'left'
            }}
            title="Left Image Card"
            subtitle="Modern workspace"
          >
            <p>This card has an image on the left side. Perfect for horizontal layouts and list items.</p>
          </Card>

          <Card
            image={{
              src: 'https://images.unsplash.com/photo-1461749280684-dccba630e2f6?w=800',
              alt: 'Coding',
              position: 'cover'
            }}
            title="Cover Image Card"
            subtitle="Full background image"
          >
            <p>This card has a full cover image with gradient overlay for text readability.</p>
          </Card>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>Badges</SectionTitle>
        <ComponentGrid>
          <ComponentBox>
            <ComponentLabel>Badge Variants</ComponentLabel>
            <FlexRow>
              <Badge variant="default">Default</Badge>
              <Badge variant="primary">Primary</Badge>
              <Badge variant="success">Success</Badge>
              <Badge variant="warning">Warning</Badge>
              <Badge variant="danger">Danger</Badge>
              <Badge variant="info">Info</Badge>
            </FlexRow>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Badge Sizes</ComponentLabel>
            <FlexRow>
              <Badge size="small">Small</Badge>
              <Badge size="medium">Medium</Badge>
            </FlexRow>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Badges with Dot</ComponentLabel>
            <FlexRow>
              <Badge variant="success" dot>Active</Badge>
              <Badge variant="warning" dot>Pending</Badge>
              <Badge variant="danger" dot>Error</Badge>
            </FlexRow>
          </ComponentBox>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>Avatars</SectionTitle>
        <ComponentGrid>
          <ComponentBox>
            <ComponentLabel>Avatar Sizes</ComponentLabel>
            <FlexRow>
              <Avatar name="John Doe" size="small" />
              <Avatar name="John Doe" size="medium" />
              <Avatar name="John Doe" size="large" />
              <Avatar name="John Doe" size="xlarge" />
            </FlexRow>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Avatar Shapes</ComponentLabel>
            <FlexRow>
              <Avatar name="Jane Smith" shape="circle" size="large" />
              <Avatar name="Jane Smith" shape="square" size="large" />
            </FlexRow>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Avatar with Status</ComponentLabel>
            <FlexRow>
              <Avatar name="User Online" status="online" size="large" />
              <Avatar name="User Away" status="away" size="large" />
              <Avatar name="User Busy" status="busy" size="large" />
              <Avatar name="User Offline" status="offline" size="large" />
            </FlexRow>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Avatar with Image</ComponentLabel>
            <FlexRow>
              <Avatar 
                src="https://via.placeholder.com/150" 
                alt="User Avatar"
                size="large"
              />
              <Avatar 
                src="https://via.placeholder.com/150" 
                alt="User Avatar"
                size="large"
                status="online"
              />
            </FlexRow>
          </ComponentBox>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>Carousel</SectionTitle>
        <ComponentGrid style={{ gridTemplateColumns: '1fr' }}>
          <ComponentBox>
            <ComponentLabel>Basic Carousel with Auto Play</ComponentLabel>
            <Carousel
              items={[
                {
                  id: 1,
                  content: (
                    <Card variant="elevated" fullWidth style={{ height: '100%' }}>
                      <h2>Slide 1</h2>
                      <p>This is the first slide with some content.</p>
                    </Card>
                  )
                },
                {
                  id: 2,
                  content: (
                    <Card variant="elevated" fullWidth style={{ height: '100%' }}>
                      <h2>Slide 2</h2>
                      <p>This is the second slide with different content.</p>
                    </Card>
                  )
                },
                {
                  id: 3,
                  content: (
                    <Card variant="elevated" fullWidth style={{ height: '100%' }}>
                      <h2>Slide 3</h2>
                      <p>This is the third slide. The carousel loops infinitely.</p>
                    </Card>
                  )
                }
              ]}
              autoPlay
              autoPlayInterval={4000}
              infiniteLoop
              height="300px"
            />
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Image Carousel</ComponentLabel>
            <Carousel
              items={[
                {
                  id: 'img1',
                  content: (
                    <img 
                      src="https://images.unsplash.com/photo-1629904853716-f0bc54eea481?w=1200" 
                      alt="Developer workspace"
                      style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                    />
                  )
                },
                {
                  id: 'img2',
                  content: (
                    <img 
                      src="https://images.unsplash.com/photo-1587620962725-abab7fe55159?w=1200" 
                      alt="Programming code"
                      style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                    />
                  )
                },
                {
                  id: 'img3',
                  content: (
                    <img 
                      src="https://images.unsplash.com/photo-1542831371-29b0f74f9713?w=1200" 
                      alt="Code editor"
                      style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                    />
                  )
                }
              ]}
              showIndicators
              showArrows
              height="400px"
            />
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Content Carousel without Controls</ComponentLabel>
            <Carousel
              items={[
                {
                  id: 'content1',
                  content: (
                    <div style={{ padding: '40px', textAlign: 'center' }}>
                      <Avatar name="John Doe" size="xlarge" />
                      <h3 style={{ marginTop: '16px' }}>John Doe</h3>
                      <Badge variant="primary">Senior Developer</Badge>
                      <p style={{ marginTop: '16px', color: '#8a8f98' }}>
                        "This design system has transformed how we build interfaces."
                      </p>
                    </div>
                  )
                },
                {
                  id: 'content2',
                  content: (
                    <div style={{ padding: '40px', textAlign: 'center' }}>
                      <Avatar name="Jane Smith" size="xlarge" />
                      <h3 style={{ marginTop: '16px' }}>Jane Smith</h3>
                      <Badge variant="success">Product Designer</Badge>
                      <p style={{ marginTop: '16px', color: '#8a8f98' }}>
                        "Clean, modern, and highly customizable components."
                      </p>
                    </div>
                  )
                }
              ]}
              showIndicators={false}
              showArrows={false}
              autoPlay
              autoPlayInterval={3000}
              infiniteLoop
              height="250px"
            />
          </ComponentBox>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>Navigation</SectionTitle>
        <ComponentGrid style={{ gridTemplateColumns: '1fr' }}>
          <ComponentBox>
            <ComponentLabel>Default Navbar</ComponentLabel>
            <div style={{ margin: '-20px', border: '1px solid #23252a', borderRadius: '8px' }}>
              <Navbar
                logo={<span style={{ fontSize: '24px' }}>⚡</span>}
                title="Linear"
                links={[
                  { label: 'Home', href: '/', isActive: true },
                  { label: 'Features', href: '/features' },
                  { label: 'Pricing', href: '/pricing', badge: 'New' },
                  { label: 'Docs', href: '/docs' },
                ]}
                rightContent={
                  <>
                    <Button variant="ghost" size="small">Sign In</Button>
                    <Button variant="primary" size="small">Get Started</Button>
                  </>
                }
              />
            </div>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Transparent Navbar (becomes solid on scroll)</ComponentLabel>
            <div style={{ margin: '-20px', border: '1px solid #23252a', borderRadius: '8px', height: '200px', overflow: 'auto' }}>
              <Navbar
                logo={<span style={{ fontSize: '24px' }}>🚀</span>}
                title="StartupOS"
                links={[
                  { label: 'Product', href: '/product' },
                  { label: 'Solutions', href: '/solutions' },
                  { label: 'Resources', href: '/resources' },
                ]}
                transparent
                sticky
                rightContent={<Button variant="primary" size="small">Try Free</Button>}
              />
              <div style={{ height: '500px', padding: '80px 20px', background: 'linear-gradient(to bottom, #5e6ad2, #08090a)' }}>
                <p>Scroll to see navbar change</p>
              </div>
            </div>
          </ComponentBox>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>Hero Sections</SectionTitle>
        <ComponentGrid style={{ gridTemplateColumns: '1fr' }}>
          <ComponentBox>
            <ComponentLabel>Default Hero with Side Image</ComponentLabel>
            <div style={{ margin: '-20px', border: '1px solid #23252a', borderRadius: '8px', overflow: 'hidden' }}>
              <Hero
                title="Build Better Products Faster"
                subtitle="Developer First"
                description="The modern development platform that helps teams ship faster with confidence. Start building today with our powerful tools."
                primaryAction={<Button variant="primary" size="large">Start Building</Button>}
                secondaryAction={<Button variant="ghost" size="large">View Demo</Button>}
                image={{
                  src: 'https://images.unsplash.com/photo-1551434678-e076c223a692?w=800',
                  alt: 'Team collaboration',
                  position: 'right'
                }}
                height="small"
              />
            </div>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Centered Hero with Background Effect</ComponentLabel>
            <div style={{ margin: '-20px', border: '1px solid #23252a', borderRadius: '8px', overflow: 'hidden' }}>
              <Hero
                title="Welcome to the Future"
                subtitle="Innovation Starts Here"
                description="Join thousands of developers building the next generation of applications."
                primaryAction={<Button variant="primary" size="large">Get Started Free</Button>}
                variant="centered"
                backgroundEffect="grid"
                height="small"
              />
            </div>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Gradient Hero</ComponentLabel>
            <div style={{ margin: '-20px', border: '1px solid #23252a', borderRadius: '8px', overflow: 'hidden' }}>
              <Hero
                title="Ship Code That Matters"
                description="Focus on what's important. We handle the rest."
                primaryAction={<Button variant="primary" size="large">Start Now</Button>}
                variant="gradient"
                height="small"
              />
            </div>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Minimal Hero</ComponentLabel>
            <div style={{ margin: '-20px', border: '1px solid #23252a', borderRadius: '8px', overflow: 'hidden' }}>
              <Hero
                title="Simple. Fast. Powerful."
                primaryAction={<Button variant="primary" size="large">Explore</Button>}
                variant="minimal"
                backgroundEffect="dots"
                height="small"
              />
            </div>
          </ComponentBox>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>Footer</SectionTitle>
        <ComponentGrid style={{ gridTemplateColumns: '1fr' }}>
          <ComponentBox>
            <ComponentLabel>Default Footer</ComponentLabel>
            <div style={{ margin: '-20px', border: '1px solid #23252a', borderRadius: '8px' }}>
              <Footer
                logo={<span style={{ fontSize: '24px' }}>⚡</span>}
                title="Linear"
                description="The issue tracking tool you'll enjoy using. Built for modern software teams."
                sections={[
                  {
                    title: 'Product',
                    links: [
                      { label: 'Features', href: '/features' },
                      { label: 'Integrations', href: '/integrations' },
                      { label: 'Pricing', href: '/pricing' },
                      { label: 'Changelog', href: '/changelog' },
                    ]
                  },
                  {
                    title: 'Company',
                    links: [
                      { label: 'About', href: '/about' },
                      { label: 'Blog', href: '/blog' },
                      { label: 'Careers', href: '/careers' },
                      { label: 'Contact', href: '/contact' },
                    ]
                  },
                  {
                    title: 'Resources',
                    links: [
                      { label: 'Documentation', href: '/docs' },
                      { label: 'API Reference', href: '/api' },
                      { label: 'Community', href: '/community' },
                      { label: 'Support', href: '/support' },
                    ]
                  }
                ]}
                socialLinks={[
                  { name: 'Twitter', href: '#', icon: <span>𝕏</span> },
                  { name: 'GitHub', href: '#', icon: <span>GH</span> },
                  { name: 'Discord', href: '#', icon: <span>DC</span> },
                ]}
                bottomText="© 2025 Linear. All rights reserved."
                bottomLinks={[
                  { label: 'Privacy Policy', href: '/privacy' },
                  { label: 'Terms of Service', href: '/terms' },
                  { label: 'Cookie Policy', href: '/cookies' },
                ]}
              />
            </div>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Minimal Footer</ComponentLabel>
            <div style={{ margin: '-20px', border: '1px solid #23252a', borderRadius: '8px' }}>
              <Footer
                variant="minimal"
                bottomText="© 2025 Your Company. All rights reserved."
                bottomLinks={[
                  { label: 'Privacy', href: '/privacy' },
                  { label: 'Terms', href: '/terms' },
                ]}
              />
            </div>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Centered Footer</ComponentLabel>
            <div style={{ margin: '-20px', border: '1px solid #23252a', borderRadius: '8px' }}>
              <Footer
                variant="centered"
                logo={<span style={{ fontSize: '32px' }}>🎯</span>}
                title="Centered Brand"
                description="Making the web a better place, one component at a time."
                socialLinks={[
                  { name: 'Twitter', href: '#', icon: <span>𝕏</span> },
                  { name: 'LinkedIn', href: '#', icon: <span>in</span> },
                  { name: 'Instagram', href: '#', icon: <span>IG</span> },
                ]}
                bottomText="Made with love by developers, for developers"
              />
            </div>
          </ComponentBox>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>Layout</SectionTitle>
        <ComponentGrid style={{ gridTemplateColumns: '1fr' }}>
          <ComponentBox>
            <ComponentLabel>Default Layout with Navbar and Footer</ComponentLabel>
            <div style={{ margin: '-20px', border: '1px solid #23252a', borderRadius: '8px', height: '600px', overflow: 'auto' }}>
              <Layout
                navbar={{
                  logo: <span style={{ fontSize: '24px' }}>📦</span>,
                  title: 'MyApp',
                  links: [
                    { label: 'Home', href: '/', isActive: true },
                    { label: 'About', href: '/about' },
                    { label: 'Contact', href: '/contact' },
                  ],
                  rightContent: <Button variant="primary" size="small">Sign In</Button>
                }}
                footer={{
                  variant: 'minimal',
                  bottomText: '© 2025 MyApp. All rights reserved.',
                  bottomLinks: [
                    { label: 'Privacy', href: '/privacy' },
                    { label: 'Terms', href: '/terms' },
                  ]
                }}
                variant="default"
              >
                <div style={{ padding: '40px 0' }}>
                  <h2>Main Content Area</h2>
                  <p>This is the main content wrapped in a Layout component with navbar and footer.</p>
                  <Card style={{ marginTop: '20px' }}>
                    <h3>Content Card</h3>
                    <p>The layout automatically handles spacing and responsive design.</p>
                  </Card>
                </div>
              </Layout>
            </div>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Dashboard Layout with Sidebar</ComponentLabel>
            <div style={{ margin: '-20px', border: '1px solid #23252a', borderRadius: '8px', height: '600px', overflow: 'hidden' }}>
              <Layout
                navbar={{
                  logo: <span style={{ fontSize: '24px' }}>⚙️</span>,
                  title: 'Dashboard',
                  links: [],
                  rightContent: <Avatar name="John Doe" size="small" />
                }}
                sidebar={{
                  position: 'left',
                  width: '240px',
                  content: (
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                      <Button variant="ghost" fullWidth style={{ justifyContent: 'flex-start' }}>
                        🏠 Dashboard
                      </Button>
                      <Button variant="ghost" fullWidth style={{ justifyContent: 'flex-start' }}>
                        📊 Analytics
                      </Button>
                      <Button variant="ghost" fullWidth style={{ justifyContent: 'flex-start' }}>
                        👥 Users
                      </Button>
                      <Button variant="ghost" fullWidth style={{ justifyContent: 'flex-start' }}>
                        ⚙️ Settings
                      </Button>
                    </div>
                  )
                }}
                variant="dashboard"
              >
                <div>
                  <h2>Dashboard Content</h2>
                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px', marginTop: '24px' }}>
                    <Card>
                      <h4>Total Users</h4>
                      <p style={{ fontSize: '24px', fontWeight: 'bold' }}>1,234</p>
                    </Card>
                    <Card>
                      <h4>Active Sessions</h4>
                      <p style={{ fontSize: '24px', fontWeight: 'bold' }}>89</p>
                    </Card>
                    <Card>
                      <h4>Revenue</h4>
                      <p style={{ fontSize: '24px', fontWeight: 'bold' }}>$12,345</p>
                    </Card>
                  </div>
                </div>
              </Layout>
            </div>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Fluid Layout</ComponentLabel>
            <div style={{ margin: '-20px', border: '1px solid #23252a', borderRadius: '8px', height: '400px', overflow: 'auto' }}>
              <Layout variant="fluid" backgroundColor="#1c1c1f">
                <div style={{ padding: '40px' }}>
                  <h2>Fluid Layout</h2>
                  <p>This layout uses the full width of its container with consistent padding.</p>
                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '20px', marginTop: '24px' }}>
                    <Card variant="outlined">Content Block 1</Card>
                    <Card variant="outlined">Content Block 2</Card>
                    <Card variant="outlined">Content Block 3</Card>
                  </div>
                </div>
              </Layout>
            </div>
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Contained Layout</ComponentLabel>
            <div style={{ margin: '-20px', border: '1px solid #23252a', borderRadius: '8px', height: '400px', overflow: 'auto' }}>
              <Layout variant="contained">
                <article style={{ padding: '40px 0' }}>
                  <h1>Article Title</h1>
                  <p style={{ marginTop: '16px', lineHeight: '1.8' }}>
                    The contained layout is perfect for prose content like blog posts or documentation. 
                    It limits the maximum width to ensure optimal reading experience.
                  </p>
                  <p style={{ marginTop: '16px', lineHeight: '1.8' }}>
                    This layout variant centers the content and adds appropriate padding, making it 
                    ideal for text-heavy pages where readability is the primary concern.
                  </p>
                </article>
              </Layout>
            </div>
          </ComponentBox>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>List & Data Display</SectionTitle>
        <ComponentGrid style={{ gridTemplateColumns: '1fr' }}>
          <ComponentBox>
            <ComponentLabel>Table List with Sorting and Selection</ComponentLabel>
            {(() => {
              const [selectedItems, setSelectedItems] = useState<any[]>([]);
              const [sortBy, setSortBy] = useState<string>('name');
              const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('asc');
              
              const sampleData = [
                { id: 1, name: 'John Doe', email: 'john@example.com', role: 'Admin', status: 'active', joinDate: '2024-01-15' },
                { id: 2, name: 'Jane Smith', email: 'jane@example.com', role: 'Developer', status: 'active', joinDate: '2024-02-20' },
                { id: 3, name: 'Bob Johnson', email: 'bob@example.com', role: 'Designer', status: 'inactive', joinDate: '2024-03-10' },
                { id: 4, name: 'Alice Brown', email: 'alice@example.com', role: 'Manager', status: 'active', joinDate: '2024-01-05' },
                { id: 5, name: 'Charlie Wilson', email: 'charlie@example.com', role: 'Developer', status: 'active', joinDate: '2024-04-01' },
              ];
              
              const columns: ListColumn[] = [
                { 
                  key: 'name', 
                  label: 'Name', 
                  sortable: true,
                  render: (value, item) => (
                    <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                      <Avatar name={value} size="small" />
                      <span>{value}</span>
                    </div>
                  )
                },
                { key: 'email', label: 'Email', sortable: true },
                { key: 'role', label: 'Role', sortable: true },
                { 
                  key: 'status', 
                  label: 'Status',
                  render: (value) => (
                    <Badge variant={value === 'active' ? 'success' : 'default'} dot>
                      {value}
                    </Badge>
                  )
                },
                { key: 'joinDate', label: 'Join Date', align: 'right' },
              ];
              
              const handleSort = (key: string) => {
                if (sortBy === key) {
                  setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
                } else {
                  setSortBy(key);
                  setSortDirection('asc');
                }
              };
              
              return (
                <List
                  data={sampleData}
                  columns={columns}
                  variant="default"
                  selectable
                  selectedItems={selectedItems}
                  onSelectionChange={setSelectedItems}
                  sortBy={sortBy}
                  sortDirection={sortDirection}
                  onSort={handleSort}
                  actions={[
                    { label: 'Edit', onClick: (item) => alert(`Edit ${item.name}`) },
                    { label: 'Delete', variant: 'danger', onClick: (item) => alert(`Delete ${item.name}`) },
                  ]}
                />
              );
            })()}
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Striped List</ComponentLabel>
            {(() => {
              const projectData = [
                { name: 'Project Alpha', status: 'In Progress', progress: 75, deadline: '2024-12-31' },
                { name: 'Project Beta', status: 'Planning', progress: 20, deadline: '2025-01-15' },
                { name: 'Project Gamma', status: 'Completed', progress: 100, deadline: '2024-11-30' },
                { name: 'Project Delta', status: 'On Hold', progress: 45, deadline: '2025-02-28' },
              ];
              
              const columns: ListColumn[] = [
                { key: 'name', label: 'Project Name' },
                { 
                  key: 'status', 
                  label: 'Status',
                  render: (value) => {
                    const variant = value === 'Completed' ? 'success' : 
                                   value === 'In Progress' ? 'primary' :
                                   value === 'On Hold' ? 'warning' : 'default';
                    return <Badge variant={variant}>{value}</Badge>;
                  }
                },
                { 
                  key: 'progress', 
                  label: 'Progress',
                  render: (value) => (
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <div style={{ flex: 1, height: '4px', background: '#232326', borderRadius: '2px' }}>
                        <div style={{ width: `${value}%`, height: '100%', background: '#5e6ad2', borderRadius: '2px' }} />
                      </div>
                      <span style={{ fontSize: '12px', color: '#8a8f98' }}>{value}%</span>
                    </div>
                  )
                },
                { key: 'deadline', label: 'Deadline', align: 'right' },
              ];
              
              return (
                <List
                  data={projectData}
                  columns={columns}
                  variant="striped"
                  size="small"
                />
              );
            })()}
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Card List View</ComponentLabel>
            {(() => {
              const cardData = [
                { id: 1, title: 'Task 1', description: 'Complete the homepage design', assignee: 'John Doe', priority: 'High', dueDate: '2024-12-20' },
                { id: 2, title: 'Task 2', description: 'Implement authentication', assignee: 'Jane Smith', priority: 'Medium', dueDate: '2024-12-22' },
                { id: 3, title: 'Task 3', description: 'Write API documentation', assignee: 'Bob Johnson', priority: 'Low', dueDate: '2024-12-25' },
                { id: 4, title: 'Task 4', description: 'Setup CI/CD pipeline', assignee: 'Alice Brown', priority: 'High', dueDate: '2024-12-21' },
              ];
              
              const columns: ListColumn[] = [
                { key: 'title', label: 'Title' },
                { key: 'description', label: 'Description' },
                { 
                  key: 'assignee', 
                  label: 'Assignee',
                  render: (value) => (
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <Avatar name={value} size="small" />
                      <span>{value}</span>
                    </div>
                  )
                },
                { 
                  key: 'priority', 
                  label: 'Priority',
                  render: (value) => {
                    const variant = value === 'High' ? 'danger' : 
                                   value === 'Medium' ? 'warning' : 'default';
                    return <Badge variant={variant}>{value}</Badge>;
                  }
                },
                { key: 'dueDate', label: 'Due Date' },
              ];
              
              return (
                <List
                  data={cardData}
                  columns={columns}
                  variant="cards"
                  onItemClick={(item) => alert(`Clicked: ${item.title}`)}
                />
              );
            })()}
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Loading State</ComponentLabel>
            <List
              data={[
                { id: 1, name: 'Item 1', value: 100 },
                { id: 2, name: 'Item 2', value: 200 },
                { id: 3, name: 'Item 3', value: 300 },
              ]}
              columns={[
                { key: 'id', label: 'ID' },
                { key: 'name', label: 'Name' },
                { key: 'value', label: 'Value', align: 'right' },
              ]}
              loading
            />
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Empty State</ComponentLabel>
            <List
              data={[]}
              columns={[
                { key: 'id', label: 'ID' },
                { key: 'name', label: 'Name' },
                { key: 'value', label: 'Value' },
              ]}
              emptyMessage="No data available. Add some items to get started."
            />
          </ComponentBox>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>Pagination</SectionTitle>
        <ComponentGrid>
          <ComponentBox>
            <ComponentLabel>Default Pagination</ComponentLabel>
            {(() => {
              const [currentPage, setCurrentPage] = useState(1);
              const [itemsPerPage, setItemsPerPage] = useState(10);
              return (
                <Pagination
                  currentPage={currentPage}
                  totalPages={20}
                  totalItems={195}
                  itemsPerPage={itemsPerPage}
                  onPageChange={setCurrentPage}
                  showItemsPerPage
                  onItemsPerPageChange={setItemsPerPage}
                />
              );
            })()}
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Compact Pagination</ComponentLabel>
            {(() => {
              const [currentPage, setCurrentPage] = useState(5);
              return (
                <Pagination
                  currentPage={currentPage}
                  totalPages={10}
                  onPageChange={setCurrentPage}
                  variant="compact"
                  showPageInfo
                  totalItems={100}
                />
              );
            })()}
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Minimal Pagination</ComponentLabel>
            {(() => {
              const [currentPage, setCurrentPage] = useState(3);
              return (
                <Pagination
                  currentPage={currentPage}
                  totalPages={8}
                  onPageChange={setCurrentPage}
                  variant="minimal"
                />
              );
            })()}
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Small Size</ComponentLabel>
            {(() => {
              const [currentPage, setCurrentPage] = useState(1);
              return (
                <Pagination
                  currentPage={currentPage}
                  totalPages={15}
                  onPageChange={setCurrentPage}
                  size="small"
                />
              );
            })()}
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Large Size</ComponentLabel>
            {(() => {
              const [currentPage, setCurrentPage] = useState(1);
              return (
                <Pagination
                  currentPage={currentPage}
                  totalPages={12}
                  onPageChange={setCurrentPage}
                  size="large"
                />
              );
            })()}
          </ComponentBox>

          <ComponentBox>
            <ComponentLabel>Limited Visible Pages</ComponentLabel>
            {(() => {
              const [currentPage, setCurrentPage] = useState(10);
              return (
                <Pagination
                  currentPage={currentPage}
                  totalPages={50}
                  onPageChange={setCurrentPage}
                  maxVisiblePages={5}
                />
              );
            })()}
          </ComponentBox>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>List with Pagination Example</SectionTitle>
        <ComponentGrid style={{ gridTemplateColumns: '1fr' }}>
          <ComponentBox>
            <ComponentLabel>Complete Data Table with Pagination</ComponentLabel>
            {(() => {
              const [currentPage, setCurrentPage] = useState(1);
              const [itemsPerPage, setItemsPerPage] = useState(5);
              const [selectedItems, setSelectedItems] = useState<any[]>([]);
              
              const allData = Array.from({ length: 50 }, (_, i) => ({
                id: i + 1,
                name: `User ${i + 1}`,
                email: `user${i + 1}@example.com`,
                department: ['Engineering', 'Design', 'Marketing', 'Sales'][i % 4],
                status: i % 3 === 0 ? 'inactive' : 'active',
                lastLogin: new Date(2024, 11, 1 + (i % 30)).toLocaleDateString(),
              }));
              
              const paginatedData = useMemo(() => {
                const startIndex = (currentPage - 1) * itemsPerPage;
                return allData.slice(startIndex, startIndex + itemsPerPage);
              }, [currentPage, itemsPerPage]);
              
              const totalPages = Math.ceil(allData.length / itemsPerPage);
              
              const columns: ListColumn[] = [
                { 
                  key: 'name', 
                  label: 'Name',
                  render: (value) => (
                    <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                      <Avatar name={value} size="small" />
                      <span>{value}</span>
                    </div>
                  )
                },
                { key: 'email', label: 'Email' },
                { key: 'department', label: 'Department' },
                { 
                  key: 'status', 
                  label: 'Status',
                  render: (value) => (
                    <Badge variant={value === 'active' ? 'success' : 'default'} dot>
                      {value}
                    </Badge>
                  )
                },
                { key: 'lastLogin', label: 'Last Login', align: 'right' },
              ];
              
              const handleItemsPerPageChange = (newItemsPerPage: number) => {
                setItemsPerPage(newItemsPerPage);
                setCurrentPage(1);
              };
              
              return (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
                  <List
                    data={paginatedData}
                    columns={columns}
                    variant="default"
                    selectable
                    selectedItems={selectedItems}
                    onSelectionChange={setSelectedItems}
                    actions={[
                      { label: 'View', onClick: (item) => alert(`View ${item.name}`) },
                    ]}
                  />
                  <div style={{ display: 'flex', justifyContent: 'center' }}>
                    <Pagination
                      currentPage={currentPage}
                      totalPages={totalPages}
                      totalItems={allData.length}
                      itemsPerPage={itemsPerPage}
                      onPageChange={setCurrentPage}
                      showPageInfo
                      showItemsPerPage
                      itemsPerPageOptions={[5, 10, 20, 50]}
                      onItemsPerPageChange={handleItemsPerPageChange}
                    />
                  </div>
                </div>
              );
            })()}
          </ComponentBox>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>Theme Information</SectionTitle>
        <ComponentGrid style={{ gridTemplateColumns: '1fr' }}>
          <div style={{ background: '#1c1c1f', padding: '24px', borderRadius: '12px', border: '1px solid #23252a' }}>
            <ThemeShowcase theme={theme} />
          </div>
        </ComponentGrid>
      </Section>

      <Section>
        <SectionTitle>Combined Examples</SectionTitle>
        <SpacedRow>
          <Card style={{ flex: 1 }}>
            <FlexRow style={{ marginBottom: '16px' }}>
              <Avatar name="Alex Johnson" status="online" />
              <div>
                <div style={{ fontWeight: 600 }}>Alex Johnson</div>
                <Badge variant="success" size="small" dot>Active</Badge>
              </div>
            </FlexRow>
            <Input 
              fullWidth
              placeholder="Type a message..."
              rightIcon={<span>📤</span>}
            />
          </Card>

          <Card variant="elevated" style={{ flex: 1 }}>
            <h3 style={{ marginBottom: '16px' }}>Quick Actions</h3>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              <Button variant="primary" fullWidth>Create New Project</Button>
              <Button variant="secondary" fullWidth>View Dashboard</Button>
              <Button variant="ghost" fullWidth>Settings</Button>
            </div>
          </Card>
        </SpacedRow>
      </Section>
    </PageContainer>
  );
}