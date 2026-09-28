import TopPicksSection from '../products/TopPicksSection';
import SavedItemsSection from '../products/SavedItemsSection';
import HotPicksSection from '../products/HotPicksSection';
import './MainContent.css';

export default function MainContent({
  topPicks,
  hotPicks,
  status,
  savedItems,
  savedItemsPreview,
  savedItemsStatus,
  viewAllSaved,
  onToggleViewAllSaved,
}) {
  return (
    <main className="main-content">
      <TopPicksSection products={topPicks} status={status} />
      {savedItems != null && (
        <SavedItemsSection
          items={savedItems}
          previewItems={savedItemsPreview}
          status={savedItemsStatus}
          viewAll={viewAllSaved}
          onToggleViewAll={onToggleViewAllSaved}
        />
      )}
      <HotPicksSection products={hotPicks} status={status} />
    </main>
  );
}
