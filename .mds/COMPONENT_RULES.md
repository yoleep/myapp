# 📐 Linear Design System Component Rules

이 문서는 프로젝트에서 Linear Design System 컴포넌트를 일관되게 사용하기 위한 필수 규칙을 정의합니다.

## 🎯 핵심 원칙

1. **컴포넌트 우선**: 항상 커스텀 HTML/CSS 대신 제공된 컴포넌트를 사용하세요.
2. **테마 일관성**: 하드코딩된 색상/스타일 대신 테마 시스템을 사용하세요.
3. **접근성**: 모든 인터랙티브 요소는 키보드 접근이 가능해야 합니다.
4. **반응형**: 모든 레이아웃은 모바일 우선으로 설계하세요.

## 🚫 금지 사항

### ❌ 하지 말아야 할 것들:

```tsx
// ❌ BAD: 직접 HTML 요소 사용
<button onClick={handleClick}>Click me</button>
<input type="text" value={value} />
<div className="card">Content</div>

// ❌ BAD: 인라인 스타일로 색상 하드코딩
<div style={{ background: '#5e6ad2', color: '#fff' }}>Content</div>

// ❌ BAD: 커스텀 CSS 클래스 생성
<div className="my-custom-button">Click</div>

// ❌ BAD: 직접 테이블 마크업
<table>
  <tr><td>Data</td></tr>
</table>
```

### ✅ 올바른 사용법:

```tsx
// ✅ GOOD: 컴포넌트 사용
<Button onClick={handleClick}>Click me</Button>
<Input value={value} onChange={setValue} />
<Card>Content</Card>

// ✅ GOOD: 테마 시스템 사용
<div style={{ 
  background: theme.colors.brand.primary, 
  color: theme.colors.brand.primaryText 
}}>Content</div>

// ✅ GOOD: List 컴포넌트로 데이터 표시
<List data={data} columns={columns} />
```

## 📋 컴포넌트별 사용 규칙

### 1. Button 컴포넌트
**필수 사용 케이스:**
- 모든 클릭 가능한 액션
- 폼 제출
- 네비게이션 트리거

```tsx
// Primary actions (중요한 액션)
<Button variant="primary">Save Changes</Button>

// Secondary actions (보조 액션)
<Button variant="secondary">Cancel</Button>

// Destructive actions (삭제/위험한 액션)
<Button variant="danger">Delete</Button>

// Ghost buttons (최소한의 스타일)
<Button variant="ghost">Learn More</Button>
```

### 2. Input 컴포넌트
**필수 사용 케이스:**
- 모든 텍스트 입력
- 폼 필드
- 검색 박스

```tsx
// 항상 label과 함께 사용
<Input 
  label="Email Address"
  type="email"
  placeholder="user@example.com"
  error={errors.email}
/>

// 아이콘이 필요한 경우
<Input 
  leftIcon={<SearchIcon />}
  placeholder="Search..."
/>
```

### 3. Card 컴포넌트
**필수 사용 케이스:**
- 콘텐츠 그룹핑
- 리스트 아이템
- 정보 블록

```tsx
// 기본 카드
<Card>
  <h3>Title</h3>
  <p>Content</p>
</Card>

// 이미지가 있는 카드
<Card 
  image={{ src: '/image.jpg', alt: 'Description' }}
  title="Card Title"
>
  Content
</Card>
```

### 4. List 컴포넌트
**필수 사용 케이스:**
- 모든 테이블 데이터
- 데이터 그리드
- 아이템 목록

```tsx
// 테이블 대신 항상 List 사용
<List 
  data={users}
  columns={columns}
  selectable
  onSelectionChange={setSelected}
/>

// 카드 뷰가 필요한 경우
<List 
  data={items}
  columns={columns}
  variant="cards"
/>
```

### 5. Layout 컴포넌트
**필수 사용 케이스:**
- 모든 페이지 레이아웃
- 대시보드
- 앱 구조

```tsx
// 페이지는 항상 Layout으로 감싸기
<Layout
  navbar={{ ... }}
  footer={{ ... }}
  variant="default"
>
  {children}
</Layout>
```

### 6. Navbar & Footer
**필수 사용 케이스:**
- 앱 네비게이션
- 페이지 헤더/푸터

```tsx
// 독립적으로 사용하지 말고 Layout과 함께 사용
<Layout
  navbar={{
    logo: <Logo />,
    links: navLinks,
    rightContent: <UserMenu />
  }}
  footer={{
    sections: footerSections,
    bottomText: '© 2025'
  }}
>
```

### 7. Select 컴포넌트
**필수 사용 케이스:**
- 모든 드롭다운
- 옵션 선택

```tsx
// native select 대신 사용
<Select 
  options={options}
  value={selected}
  onChange={setSelected}
  label="Choose Option"
/>
```

### 8. Badge 컴포넌트
**필수 사용 케이스:**
- 상태 표시
- 라벨
- 카운트 표시

```tsx
// 상태별 variant 사용
<Badge variant="success">Active</Badge>
<Badge variant="warning">Pending</Badge>
<Badge variant="danger">Error</Badge>
```

### 9. Avatar 컴포넌트
**필수 사용 케이스:**
- 사용자 프로필 이미지
- 사용자 이니셜

```tsx
// 이미지가 있을 때
<Avatar src="/user.jpg" alt="John Doe" />

// 이미지가 없을 때 (이니셜 자동 생성)
<Avatar name="John Doe" />

// 온라인 상태 표시
<Avatar name="Jane" status="online" />
```

### 10. Pagination 컴포넌트
**필수 사용 케이스:**
- 모든 페이지네이션
- 데이터 네비게이션

```tsx
// List와 함께 사용
<>
  <List data={paginatedData} columns={columns} />
  <Pagination 
    currentPage={page}
    totalPages={totalPages}
    onPageChange={setPage}
  />
</>
```

## 🎨 스타일링 규칙

### 1. 테마 사용 필수

```tsx
import { theme } from '@/styles/theme';

// ✅ GOOD
const CustomComponent = styled.div`
  background: ${props => props.theme.colors.background.secondary};
  padding: ${props => props.theme.spacing.layout.pagePaddingInline};
`;

// ❌ BAD
const CustomComponent = styled.div`
  background: #1c1c1f;
  padding: 24px;
`;
```

### 2. 반응형 디자인

```tsx
// 모바일 우선 접근
const Container = styled.div`
  padding: 16px;
  
  @media (min-width: 768px) {
    padding: 24px;
  }
  
  @media (min-width: 1024px) {
    padding: 32px;
  }
`;
```

### 3. 다크 모드 전용
- 모든 컴포넌트는 다크 모드에 최적화되어 있습니다
- 라이트 모드 스타일을 추가하지 마세요

## 📁 파일 구조 규칙

```
src/
├── components/          # 재사용 가능한 컴포넌트만
│   ├── Button/
│   ├── Input/
│   └── ...
├── pages/              # 페이지 컴포넌트
│   └── dashboard.tsx   # Layout 컴포넌트 필수 사용
├── styles/
│   ├── theme.ts        # 테마 설정 (수정 금지)
│   └── GlobalStyles.ts # 글로벌 스타일 (수정 금지)
```

## 🔧 개발 체크리스트

새로운 기능을 개발할 때 확인사항:

- [ ] Button 컴포넌트를 사용했는가? (커스텀 버튼 X)
- [ ] Input 컴포넌트를 사용했는가? (native input X)
- [ ] Card로 콘텐츠를 그룹화했는가? (커스텀 div X)
- [ ] List로 데이터를 표시했는가? (table 태그 X)
- [ ] Layout으로 페이지를 구성했는가?
- [ ] Select로 드롭다운을 구현했는가? (native select X)
- [ ] 테마 색상을 사용했는가? (하드코딩 X)
- [ ] 모바일에서 제대로 표시되는가?
- [ ] 키보드로 조작 가능한가?
- [ ] 로딩/에러 상태를 처리했는가?

## 🚀 Best Practices

### 1. 컴포넌트 조합
```tsx
// 복잡한 UI는 기본 컴포넌트 조합으로 구성
<Card>
  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
    <Avatar name={user.name} />
    <Badge variant="success">Active</Badge>
  </div>
  <h3>{user.name}</h3>
  <p>{user.email}</p>
  <Button variant="primary" fullWidth>
    View Profile
  </Button>
</Card>
```

### 2. 상태 관리
```tsx
// 페이지네이션과 데이터 연동
const [page, setPage] = useState(1);
const [itemsPerPage, setItemsPerPage] = useState(10);

const paginatedData = useMemo(() => {
  const start = (page - 1) * itemsPerPage;
  return data.slice(start, start + itemsPerPage);
}, [data, page, itemsPerPage]);
```

### 3. 에러 처리
```tsx
// Input 에러 표시
<Input 
  label="Email"
  value={email}
  onChange={setEmail}
  error={errors.email}
  helperText="Enter a valid email"
/>
```

## 📝 코드 리뷰 체크포인트

PR 리뷰 시 확인사항:
1. 커스텀 HTML 요소 사용 여부
2. 하드코딩된 색상/스타일 여부
3. 제공된 컴포넌트 미사용 여부
4. 테마 시스템 미사용 여부
5. 반응형 디자인 미적용 여부

## 🛠 마이그레이션 가이드

기존 코드를 Linear Design System으로 마이그레이션:

```tsx
// Before
<button className="btn btn-primary">Submit</button>

// After
<Button variant="primary">Submit</Button>

// Before
<div className="card">
  <h3>Title</h3>
</div>

// After
<Card>
  <h3>Title</h3>
</Card>

// Before
<table>...</table>

// After
<List data={data} columns={columns} />
```

## 📞 지원

컴포넌트 사용에 대한 질문이 있으면:
1. `/components` 페이지에서 데모 확인
2. 각 컴포넌트의 TypeScript 타입 정의 참고
3. `COMPONENT_RULES.md` (이 문서) 참고

---

**⚠️ 중요: 이 규칙들은 프로젝트의 일관성과 유지보수성을 위해 필수입니다. 예외 없이 준수해주세요.**